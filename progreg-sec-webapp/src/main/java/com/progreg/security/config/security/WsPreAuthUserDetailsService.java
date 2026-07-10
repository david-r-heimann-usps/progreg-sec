package com.progreg.security.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WsPreAuthUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
	private JdbcTemplate jdbcTemplate;

	public WsPreAuthUserDetailsService(DataSource dataSource)
	{
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
		// TODO Auto-generated method stub
		System.out.println("I fight authority, authority always wins.");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	    String url = request.getRequestURL().toString();
	    String redirectUri = request.getParameter("redirect_uri"); // if present	
		List<SimpleGrantedAuthority> sgas = new  ArrayList<SimpleGrantedAuthority>();		
		String sql = "select cap_name from auth_intr_role_cap_view where upper(role_name) = ?";
		int[] paramTypes = {java.sql.Types.VARCHAR};
		for(GrantedAuthority ga : authorities)
		{
			String role = ga.getAuthority();
			if(role.startsWith("ROLE_NAT_PROGREG_"))
			{
				role = role.substring("ROLE_NAT_PROGREG_".length());
			}
			if(role.startsWith("NAT_PROGREG_"))
			{
				role = role.substring("NAT_PROGREG_".length());
			}
			Object[] params = {role.toUpperCase()};
			if(!isAllowed(role, url))
			{
				continue;
			}
			List<String> capNames = jdbcTemplate.queryForList(sql,params, paramTypes, String.class );
			for(String capName : capNames)
			{
				sgas.add(new SimpleGrantedAuthority(capName));
			}
		}
		if(sgas.isEmpty())
		{
			throw new RuntimeException("No capabilities found for authorities " + authorities);
		}
		return sgas;
	}

	
	private boolean isAllowed(String roleName, String url)
	{
		System.out.println("URL is " + url);
		if(url.contains("progreg-sec") && roleName.contains("CONTRACT") && !containsOther(roleName))
		{
			return true;
		}
		if(url.contains("-gca-") && roleName.contains("GCA"))
		{
			return true;
		}
		if(url.contains("-geps-") && roleName.contains("GEPS"))
		{
			return true;
		}
		if(url.contains("-cpp-") && roleName.contains("CPP"))
		{
			return true;
		}
		if(url.contains("-sharemail-") && roleName.contains("SHAREMAIL"))
		{
			return true;
		}
		if(url.contains("-contract-") && roleName.contains("CONTRACT") && !containsOther(roleName))
		{
			return true;
		}
		if(url.contains("-internal-") 
				&& !(
				roleName.contains("CONTRACT") || containsOther(roleName)
				)
			)
		{
			return true;
		}
		return false;
	}
	private boolean containsOther(String roleName)
	{
		return 	roleName.contains("CPP") 
				|| roleName.contains("SHAREMAIL") 
				|| roleName.contains("GEPS") 
				|| roleName.contains("GCA");

	}
	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
		System.out.println("Token is:");
		System.out.println(token);
		String username = token.getName();
		PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails details 
		= (PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails)token.getDetails();
		UserDetails ud = User.withUsername(username)
	            .password("na")
	            .authorities(mapAuthorities(details.getGrantedAuthorities()))
	            .accountExpired(false)
	            .accountLocked(false)
	            .credentialsExpired(false)
	            .disabled(false)
	            .build();
		return ud;
	}

}
