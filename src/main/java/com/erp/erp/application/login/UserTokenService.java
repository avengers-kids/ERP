package com.erp.erp.application.login;

import com.erp.erp.domain.model.user.UserToken;
import com.erp.erp.domain.model.user.UserTokenRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserTokenService {

    private final UserTokenRepository repo;

    public UserTokenService(UserTokenRepository repo) {
        this.repo = repo;
    }

    /**
     * Save a new token:
     * 1) Revoke any existing active tokens for this user.
     * 2) Persist the new token record.
     */
    @Transactional
    public void saveToken(String username, String jti, Date issuedAt, Date expiresAt) {
        List<UserToken> active = repo.findAllByUsernameAndRevokedFalse(username);
        for (UserToken t : active) {
            t.setRevoked(true);
        }
        repo.saveAll(active);

        UserToken newToken = new UserToken(
            username,
            jti,
            toLocalDateTime(issuedAt),
            toLocalDateTime(expiresAt)
        );
        repo.save(newToken);
    }

    /**
     * Revoke a single token (e.g. when refreshing)
     */
    @Transactional
    public void revokeToken(String username, String jti) {
        Optional<UserToken> tok = repo.findByUsernameAndJti(username, jti);
        tok.ifPresent(t -> {
            t.setRevoked(true);
            repo.save(t);
        });
    }

    /**
     * Check that the token is:
     *  - present for this user
     *  - not marked revoked
     *  - not expired (expiresAt > now)
     */
    public boolean isTokenActive(String username, String jti) {
        return repo.existsByUsernameAndJtiAndRevokedFalseAndExpiresAtAfter(
            username, jti, LocalDateTime.now()
        );
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toLocalDateTime();
    }
}
