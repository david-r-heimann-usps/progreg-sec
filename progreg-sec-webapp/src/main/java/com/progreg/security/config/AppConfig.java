package com.progreg.security.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC web-tier configuration loaded by the DispatcherServlet.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.progreg.security.controller")
public class AppConfig implements WebMvcConfigurer {
}
