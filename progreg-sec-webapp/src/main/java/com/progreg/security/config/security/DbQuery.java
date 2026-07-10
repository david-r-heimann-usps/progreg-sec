package com.progreg.security.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public class DbQuery implements UserDetailsService {
	
	public DbQuery(DataSource dataSource)
	{
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	JdbcTemplate jdbcTemplate;


	@Override
	@Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Object[] params = {username.toUpperCase()};
		int[] paramTypes = {java.sql.Types.VARCHAR};
		String sql = "select  role_name from auth_intr_user_role_view where upper(login_id) = ?";
		String sqlCap = "select  cap_name from auth_intr_role_cap_view where upper(role_name) = ?";
		List<String> roleNames = jdbcTemplate.queryForList(sql, params, paramTypes, String.class);
		List<String> capNames = new ArrayList<String>();
		for(String roleName : roleNames)
		{
			Object[] roleParams = {roleName.toUpperCase()};
			List<String> dbCapNames = jdbcTemplate.queryForList(sqlCap, roleParams, paramTypes, String.class);
			capNames.addAll(dbCapNames);
		}
		List<String> revisedCapNames = new ArrayList<String>();
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

		for(String capName :capNames)			
		{	
			if(!revisedCapNames.contains(capName))
			{
				revisedCapNames.add(capName);
				grantedAuthorities.add(new SimpleGrantedAuthority("CAP_" + capName));
			}
		}
		
		UserDetails ud = User.withUsername(username)
	            .password("$2a$12$yVFw/upukvo4Ba8JpDUw6eTuipcJqhXwo9/3JhKHjC0WEuOEdPZ7q")
	            .authorities(grantedAuthorities)
	            .accountExpired(false)
	            .accountLocked(false)
	            .credentialsExpired(false)
	            .disabled(false)
	            .build();
		if(ud.getAuthorities().isEmpty())
		{
			throw new AuthenticationCredentialsNotFoundException("User has no Program Registration roles");
		}
		return ud;
    }
}