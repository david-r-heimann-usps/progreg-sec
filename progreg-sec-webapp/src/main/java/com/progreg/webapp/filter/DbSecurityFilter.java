package com.progreg.webapp.filter;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.progreg.security.config.security.WsPreAuthUserDetailsService;

public class DbSecurityFilter extends OncePerRequestFilter
{

	private final WsPreAuthUserDetailsService authoritiesMapper;

    public DbSecurityFilter(WsPreAuthUserDetailsService authoritiesMapper) {
        this.authoritiesMapper = authoritiesMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        Authentication existing = SecurityContextHolder.getContext().getAuthentication();
        
        if (existing != null && existing.isAuthenticated()
                && !(existing instanceof AnonymousAuthenticationToken)) {
            PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails details =
                    (PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails)existing.getDetails();	

            // Re-map authorities for THIS WAR on every request
            Collection<? extends GrantedAuthority> newAuthorities =
                authoritiesMapper.mapAuthorities(details.getGrantedAuthorities());
            
            // Rebuild the Authentication with this WAR's authorities
            UsernamePasswordAuthenticationToken newAuth =
                new UsernamePasswordAuthenticationToken(
                    existing.getPrincipal(),
                    existing.getCredentials(),
                    newAuthorities
                );
            newAuth.setDetails(existing.getDetails());

            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }

        chain.doFilter(request, response);
    }}