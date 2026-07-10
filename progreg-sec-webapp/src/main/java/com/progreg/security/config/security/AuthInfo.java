package com.progreg.security.config.security;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;
import org.springframework.ui.Model;

public class AuthInfo {

	public static void fillModel(Model model)
	{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "null";
        Collection<? extends GrantedAuthority> authorities = authentication != null
                ? authentication.getAuthorities()
                : Collections.emptyList();

        model.addAttribute("message", "Hello from Spring MVC");
        model.addAttribute("username", username);
        model.addAttribute("roles", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        if(authentication.getDetails() instanceof PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails ) {
        	PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails details =
                (PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails)authentication.getDetails();	
        	model.addAttribute("token", details.getGrantedAuthorities());
        }
        else
        {
            model.addAttribute("token", authentication);
    	
        }
	}
}
