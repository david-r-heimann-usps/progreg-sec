package com.progreg.security.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the {@code websphere} profile.
 *
 * <p>Delegates authentication entirely to WebSphere container-managed security
 * (LTPA / SPNEGO / TAI).  Spring Security reads the authenticated principal and
 * J2EE roles from the servlet container via the JEE pre-authentication filter
 * ({@code jee()}).
 *
 * <p>Role names listed in {@code mappableRoles} must match the security-role
 * entries defined in the WebSphere application bindings.  Add or remove roles
 * to match your WebSphere security configuration.
 */
@Configuration
@Profile("websphere")
public class WebSphereSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/public/**", "/error", "/logoff-success").permitAll()
                .anyRequest().authenticated()
            )
            .jee(jee -> jee
                .mappableRoles("USER", "ADMIN", "MANAGER")
            );
        return http.build();
    }
}
