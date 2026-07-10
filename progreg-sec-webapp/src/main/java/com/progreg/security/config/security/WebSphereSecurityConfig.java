package com.progreg.security.config.security;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.progreg.webapp.filter.WsSecurityFilter;

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
    public DataSource progregDataSource() {
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        lookup.setResourceRef(true);
        return lookup.getDataSource("jdbc/progreg");
    }
 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        	http
    		.authorizeHttpRequests(auth -> auth
                .antMatchers("/login-ws.jsp").permitAll() 
                .antMatchers("/j_security_check").permitAll()
                .antMatchers("/ibm_security_logout").permitAll()
                
                .anyRequest().authenticated()
            )
	        .csrf(csrf -> csrf
	                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
	                //.ignoringRequestMatchers(new AntPathRequestMatcher("/j_security_check"))
            )
	        .jee(jee -> jee
	                .mappableAuthorities(getRoles(progregDataSource()))
	         )
	        .authenticationProvider(preAuthenticatedAuthenticationProvider())
        	.addFilterAfter(new WsSecurityFilter(new WsPreAuthUserDetailsService(progregDataSource())), SecurityContextPersistenceFilter.class);
        return http.build();
    }
    
    @Bean
    public AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> authenticationUserDetailsService() {
        return new WsPreAuthUserDetailsService(progregDataSource());
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(authenticationUserDetailsService());
        return provider;
    }
  
	private Set<String> getRoles(DataSource progregDataSource) {
		String sql = "select distinct 'NAT_PROGREG_' || role_name from auth_intr_role_cap_view";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(progregDataSource);
		List<String> roleNames = jdbcTemplate.queryForList(sql, String.class);
		Set<String> roleNameSet = roleNames.stream().collect(Collectors.toSet());
		return roleNameSet;
	}
}
