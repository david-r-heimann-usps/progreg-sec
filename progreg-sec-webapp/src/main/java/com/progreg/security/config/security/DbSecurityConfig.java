package com.progreg.security.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

/**
 * Spring Security configuration for the {@code db} profile.
 *
 * <p>Authenticates and authorises users against the database that is exposed
 * through the JNDI datasource named {@code jdbc/progreg} (defined in WebSphere
 * and bound via {@code ibm-web-bnd.xml}).
 *
 * <p>The datasource must contain the standard Spring Security JDBC schema:
 * <ul>
 *   <li>{@code users(username, password, enabled)}</li>
 *   <li>{@code authorities(username, authority)}</li>
 * </ul>
 * Use {@code spring-security-core}'s
 * {@code org/springframework/security/core/userdetails/jdbc/users.ddl} as a
 * starting point for the DDL.
 */
@Configuration
@Profile("db")
public class DbSecurityConfig {

    /**
     * Looks up the {@code jdbc/progreg} datasource from JNDI.
     * {@code setResourceRef(true)} prepends {@code java:comp/env/} automatically,
     * which is required for resource-ref lookups in a Java EE container.
     */
    @Bean
    public DataSource progregDataSource() {
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        lookup.setResourceRef(true);
        return lookup.getDataSource("jdbc/progreg");
    }

    /**
     * BCrypt password encoder.  Passwords stored in the {@code users} table must
     * be BCrypt-hashed.  Use {@code passwordEncoder.encode(rawPassword)} when
     * inserting or updating user records.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Standard JDBC-backed {@link org.springframework.security.core.userdetails.UserDetailsService}
     * and user-management service backed by {@code progregDataSource}.
     */
    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource progregDataSource) {
        return new JdbcUserDetailsManager(progregDataSource);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/public/**", "/error", "/login", "/logoff-success").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            );
        return http.build();
    }
}
