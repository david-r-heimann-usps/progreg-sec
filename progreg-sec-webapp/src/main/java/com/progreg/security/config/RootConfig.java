package com.progreg.security.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Root Spring application context.
 * Loads security configuration classes found under
 * com.progreg.security.config.security; Spring's @Profile annotations
 * on each config class ensure only the active-profile's beans are created.
 */
@Configuration
@ComponentScan(basePackages = {
    "com.progreg.security.config.security",
    "com.progreg.security.service"
})
public class RootConfig {
}
