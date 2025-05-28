package com.erp.erp.domain.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    List<UserToken> findAllByUsernameAndRevokedFalse(String username);
    Optional<UserToken> findByUsernameAndJti(String username, String jti);
    boolean existsByUsernameAndJtiAndRevokedFalseAndExpiresAtAfter(
        String username,
        String jti,
        LocalDateTime now
    );
}
