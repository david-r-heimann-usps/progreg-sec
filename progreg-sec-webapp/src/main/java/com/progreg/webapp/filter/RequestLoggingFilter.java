package com.progreg.webapp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestLoggingFilter implements Filter
{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		System.out.println("Logging Before: " + httpServletRequest.getRequestURI());
		try {
		chain.doFilter(httpServletRequest, httpServletResponse);
		System.out.println("Logging After: " + httpServletRequest.getRequestURI());
		}
		catch(Throwable t)
		{
			System.out.println("Logging Exception: ");
			System.out.println(t);
			t.printStackTrace();
		}
		
		
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}