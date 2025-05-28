package com.erp.erp.domain.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_token")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username (email) of the user this token belongs to.
     */
    @Column(nullable = false, length = 100)
    private String username;

    /**
     * The JWT ID (jti) claim, used to uniquely identify and revoke tokens.
     */
    @Column(nullable = false, unique = true, length = 36)
    private String jti;

    /**
     * When this token was issued.
     */
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    /**
     * When this token expires.
     */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Whether this token has been revoked (e.g. on logout or refresh).
     */
    @Column(nullable = false)
    private boolean revoked = false;

    // --- constructors ---

    public UserToken() {}

    public UserToken(String username, String jti, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this.username  = username;
        this.jti       = jti;
        this.issuedAt  = issuedAt;
        this.expiresAt = expiresAt;
    }

    // --- getters & setters ---

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
