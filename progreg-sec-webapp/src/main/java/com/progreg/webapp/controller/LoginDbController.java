package com.progreg.webapp.controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginDbController {
	
	private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @RequestMapping( value = "/login-db.action", method = RequestMethod.GET )
    public String login() {
    	return "login-db";
    }
    
    @RequestMapping( value = "/logoff-db.action", method = RequestMethod.GET )
    public String logoff(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        System.out.println("Logoff called with auth = " + authentication); 
    	if (authentication != null) {
                // Clears SecurityContextHolder, invalidates session, and removes cookies
                this.logoutHandler.logout(request, response, authentication); 
            }
            return "redirect:/login-db";
    }
}
