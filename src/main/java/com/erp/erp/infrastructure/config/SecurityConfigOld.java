//package com.erp.erp.infrastructure.config;
//
//import com.erp.erp.infrastructure.component.SecurityAuditorAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.savedrequest.NullRequestCache;
//
//@Configuration
//@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
//public class SecurityConfigOld {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//
//    @Bean
//    public AuditorAware<String> auditorProvider() {
//        return new SecurityAuditorAware();
//    }
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(AbstractHttpConfigurer::disable)
//            .cors(AbstractHttpConfigurer::disable)
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(HttpMethod.POST, "/auth/").hasAnyRole("ADMIN", "USER")
//                .anyRequest().authenticated()
//            )
//            .requestCache(cache -> cache.requestCache(new NullRequestCache()))
//            .httpBasic();
//
//        return http.build();
//    }
//}
