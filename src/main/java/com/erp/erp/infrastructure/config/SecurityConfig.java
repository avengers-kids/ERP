package com.erp.erp.infrastructure.config;

import com.erp.erp.application.login.AuthService;
import com.erp.erp.application.login.UserTokenService;
import com.erp.erp.infrastructure.component.JwtUtil;
import com.erp.erp.infrastructure.utility.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
  private final JwtUtil jwtUtil;
//  private final AuthService uds;

  public SecurityConfig(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
//    this.uds = uds;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, AuthService uds, UserTokenService utds) throws Exception {
    JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil, uds, utds);

    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .sessionManagement(sm ->
            sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/signup", "/api/auth/client/signup").permitAll()
            .requestMatchers(HttpMethod.GET,  "/api/auth/**").permitAll()
            .anyRequest().authenticated()
        )
        .exceptionHandling(ex -> ex
            .accessDeniedHandler((req, res, accessDeniedException) -> {
              res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You do not have permission to access this resource");
            })
            .authenticationEntryPoint((req, res, authException) -> {
              res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            })
        )
        .requestCache(cache -> cache.requestCache(new NullRequestCache()))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder encoder, AuthService uds)
      throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
      .userDetailsService(uds)
      .passwordEncoder(encoder)
      .and()
      .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
