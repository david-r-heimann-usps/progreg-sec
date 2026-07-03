package com.progreg.security.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the {@code okta} profile.
 *
 * <p>Authenticates users via Okta as an OpenID Connect / OAuth 2.0 provider.
 * Groups are mapped from the Okta {@code groups} claim in the ID token and used
 * to populate Spring Security granted authorities.
 *
 * <p>Required application properties (set in {@code application.properties} or as
 * environment variables / WebSphere JVM custom properties):
 * <pre>
 *   spring.security.oauth2.client.registration.okta.client-id=&lt;client-id&gt;
 *   spring.security.oauth2.client.registration.okta.client-secret=&lt;client-secret&gt;
 *   spring.security.oauth2.client.registration.okta.scope=openid,profile,email,groups
 *   spring.security.oauth2.client.provider.okta.issuer-uri=https://&lt;okta-domain&gt;/oauth2/default
 * </pre>
 */
@Configuration
@Profile("okta")
public class OktaSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/public/**", "/error", "/logoff-success").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            );
        return http.build();
    }
}
