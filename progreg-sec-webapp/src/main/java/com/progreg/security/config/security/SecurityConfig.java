package com.progreg.security.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Base Spring Security configuration.
 *
 * {@code @EnableWebSecurity} registers the Spring Security infrastructure once.
 * The actual {@link org.springframework.security.web.SecurityFilterChain} bean
 * is provided by exactly one of the profile-specific subconfigurations:
 * <ul>
 *   <li>{@link OktaSecurityConfig}      – activated by Spring profile {@code okta}</li>
 *   <li>{@link WebSphereSecurityConfig} – activated by Spring profile {@code websphere}</li>
 *   <li>{@link DbSecurityConfig}        – activated by Spring profile {@code db}</li>
 * </ul>
 *
 * These classes are discovered automatically because {@link com.progreg.security.config.RootConfig}
 * component-scans the {@code com.progreg.security.config.security} package.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
}
