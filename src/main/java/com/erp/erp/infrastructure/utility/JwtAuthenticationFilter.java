package com.erp.erp.infrastructure.utility;

import com.erp.erp.application.login.UserTokenService;
import com.erp.erp.infrastructure.component.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;
  private final UserTokenService userTokenService;

  public JwtAuthenticationFilter(
      JwtUtil jwtUtil,
      UserDetailsService uds,
      UserTokenService userTokenService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = uds;
    this.userTokenService = userTokenService;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String uri = request.getRequestURI();
    String context = request.getContextPath();
    String path = context.isEmpty() ? uri : uri.substring(context.length());

    System.out.println("path uri is : " + path);

    // Skip JWT filter on any /api/auth/*
    if (path.startsWith("/api/auth/")) {
      return true;
    }

    // Skip other purely public paths:
    if (path.equals("/error") || path.equals("/health")) {
      return true;
    }
    if (path.startsWith("/public/")) {
      return true;
    }
    if (new AntPathMatcher().match("/swagger-ui/**", path)) {
      return true;
    }

    return false;
  }

  @Override
  protected void doFilterInternal(
       HttpServletRequest req,
       HttpServletResponse res,
       FilterChain chain
  ) throws ServletException, IOException {

    System.out.println("uri filter internal : " + req.getRequestURI());
    String authHeader = req.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      chain.doFilter(req, res);
      return;
    }

    String token = authHeader.substring(7).trim();
    if (token.isEmpty()) {
      logger.debug("Authorization header is present but token is empty");
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT token");
      return;
    }

    String username;
    try {
      username = jwtUtil.extractUsername(token);
    } catch (JwtException e) {
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
      return;
    }

    // If username could not be extracted or context is already authenticated, skip further checks
    if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
      chain.doFilter(req, res);
      return;
    }

    // Load UserDetails to validate token against stored credentials/roles
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    // Validate the token (signature & expiration)
    try {
      if (!jwtUtil.validateToken(token, userDetails)) {
//        logger.debug("JWT token validation failed for user {}", username);
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token validation failed");
        return;
      }
    } catch (JwtException e) {
      // Any parsing exceptions during validation
//      logger.debug("JWT validation threw exception: {}", e.getMessage());
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
      return;
    }

    // Verify token is still active (not revoked and not expired in DB)
    String jti;
    try {
      jti = jwtUtil.extractJti(token);
    } catch (JwtException e) {
//      logger.debug("Could not extract JTI from token: {}", e.getMessage());
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
      return;
    }

    boolean tokenActive = userTokenService.isTokenActive(username, jti);
    if (!tokenActive) {
//      logger.debug("JWT token with JTI={} for user={} is not active", jti, username);
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token revoked or expired");
      return;
    }

    // Build authentication and set in SecurityContext
    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
    SecurityContextHolder.getContext().setAuthentication(authToken);

    chain.doFilter(req, res);
  }
}
