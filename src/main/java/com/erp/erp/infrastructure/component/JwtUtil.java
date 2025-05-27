package com.erp.erp.infrastructure.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private SecretKey key;

  /** Access tokens valid for 1 hour */
  private static final long EXPIRATION_MS = 1000 * 60 * 60;

  @PostConstruct
  public void init() {
    // You might load this from a config property instead of auto-generating each startup
    this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
  }

  /**
   * Generates a new JWT with:
   * - a random JTI
   * - subject = username
   * - roles claim
   * - issuedAt = now
   * - expiration = now + EXPIRATION_MS
   */
  public String generateToken(UserDetails userDetails) {
    Date now = new Date();
    String jti = UUID.randomUUID().toString();

    return Jwts.builder()
        .setId(jti)
        .setSubject(userDetails.getUsername())
        .claim("roles", toRoleList(userDetails))
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + EXPIRATION_MS))
        .signWith(key)
        .compact();
  }

  /**
   * “Refreshes” an existing valid token by issuing
   * a brand-new token with the same claims but fresh timestamps.
   */
  public String refreshToken(String token) {
    Claims claims = parseAllClaims(token);
    Date now = new Date();

    return Jwts.builder()
        .setClaims(claims)  // includes subject, jti, roles, and any other custom claims
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + EXPIRATION_MS))
        .signWith(key)
        .compact();
  }

  /** Validates signature, expiration, and matching username */
  public boolean validateToken(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  /** Pull the “sub” (username) out of the token */
  public String extractUsername(String token) {
    return parseAllClaims(token).getSubject();
  }

  /** Pull the JTI out of the token */
  public String extractJti(String token) {
    return parseAllClaims(token).getId();
  }

  /** Pull the roles list out of the token */
  @SuppressWarnings("unchecked")
  public List<String> extractRoles(String token) {
    return (List<String>) parseAllClaims(token).get("roles");
  }

  /** Pull the issued-at timestamp out of the token */
  public Date extractIssuedAt(String token) {
    return parseAllClaims(token).getIssuedAt();
  }

  /** Pull the expiration timestamp out of the token */
  public Date extractExpiration(String token) {
    return parseAllClaims(token).getExpiration();
  }

  // —— Internals —— //

  private Claims parseAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private boolean isTokenExpired(String token) {
    return parseAllClaims(token)
        .getExpiration()
        .before(new Date());
  }

  private List<String> toRoleList(UserDetails userDetails) {
    return userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();
  }
}
