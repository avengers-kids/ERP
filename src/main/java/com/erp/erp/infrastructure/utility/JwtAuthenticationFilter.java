package com.erp.erp.infrastructure.utility;

import com.erp.erp.infrastructure.component.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService uds) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = uds;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();

    if (path.startsWith(request.getContextPath() + "/api/auth/login")) {
      return true;
    }
    if (path.startsWith(request.getContextPath() + "/api/auth/signup")) {
      return true;
    }
    if (path.startsWith(request.getContextPath() + "/api/auth/client/signup")) {
      return true;
    }
    if (path.equals(request.getContextPath() + "/error")) {
      return true;
    }
    if (path.startsWith(request.getContextPath() + "/public/")) {
      return true;
    }
    if (path.equals(request.getContextPath() + "/health")) {
      return true;
    }
    if (new AntPathMatcher().match("/swagger-ui/**", path)) {
      return true;
    }
    return false;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {
    String authHeader = req.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      String username = jwtUtil.extractUsername(token);
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtUtil.validateToken(token, userDetails)) {
          UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    }
    chain.doFilter(req, res);
  }
}
