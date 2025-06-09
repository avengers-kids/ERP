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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
  private final JwtUtil jwtUtil;

  public SecurityConfig(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      AuthService uds,
      UserTokenService utds) throws Exception {
    JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil, uds, utds);

    http
        // 1) Disable CSRF (you typically still want this off for a stateless REST API)
        .csrf(AbstractHttpConfigurer::disable)

        // 2) Enable CORS and point Spring to our CorsConfigurationSource bean
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))

        // 3) Make the session stateless
        .sessionManagement(sm ->
            sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        // 4) Configure which endpoints are public vs. secured
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/signup", "/api/auth/client/signup")
            .permitAll()
            .requestMatchers(HttpMethod.GET, "/api/auth/**")
            .permitAll()
            .anyRequest()
            .authenticated()
        )

        // 5) Custom “access denied” and “auth entrypoint” responses
        .exceptionHandling(ex -> ex
            .accessDeniedHandler((req, res, accessDeniedException) -> {
              res.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                  "You do not have permission to access this resource");
            })
            .authenticationEntryPoint((req, res, authException) -> {
              res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
            })
        )

        // 6) Disable request caching (remember, this is stateless)
        .requestCache(cache -> cache.requestCache(new NullRequestCache()))

        // 7) Register our JWT filter before UsernamePasswordAuthenticationFilter
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authManager(HttpSecurity http,
      PasswordEncoder encoder,
      AuthService uds) throws Exception {
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

  /**
   * Defines which origins, headers, and methods are allowed for CORS.
   * You can add as many origins as you need to this list.
   * In this example, we’re allowing two specific origins:
   *   1) http://localhost:3000
   *   2) https://my-frontend.example.com
   *
   * We also allow GET, POST, PUT, DELETE, OPTIONS, etc., and permit all headers
   * (you can lock this down further if you want).
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // 1) Whitelisted origins
    configuration.setAllowedOrigins(Arrays.asList(
        "http://localhost:3000",
        "https://my-frontend.example.com",
        ""
    ));

    // 2) Whitelisted HTTP methods
    configuration.setAllowedMethods(Arrays.asList(
        "GET",
        "POST",
        "PUT",
        "DELETE",
        "OPTIONS"
    ));

    // 3) Whitelisted headers (you can also do Arrays.asList("Authorization", "Content-Type", ...) instead)
    configuration.setAllowedHeaders(Arrays.asList("*"));

    // 4) If you need to allow cookies/credentials
    configuration.setAllowCredentials(true);

    // 5) Expose certain headers (in case client needs to read them)
    configuration.setExposedHeaders(Arrays.asList("Authorization"));

    // 6) Register this CORS configuration for all paths ("/*")
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
