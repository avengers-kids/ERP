package com.erp.erp.infrastructure.config;

import com.erp.erp.infrastructure.component.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SecurityConfig {

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SecurityAuditorAware();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/auth/").hasAnyRole("ADMIN", "USER")
            .anyRequest().authenticated()
            .and()
            .requestCache()
            .requestCache(new NullRequestCache())
            .and()
            .httpBasic();

        return http.build();
    }
}
