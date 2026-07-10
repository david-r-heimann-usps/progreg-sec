package com.progreg.webapp.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {

    @RequestMapping( value = "/hello.action", method = RequestMethod.GET )
    public String hello(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "anonymous";
        Collection<? extends GrantedAuthority> authorities = authentication != null
                ? authentication.getAuthorities()
                : Collections.emptyList();

        model.addAttribute("message", "Hello from Spring MVC + JSP");
        model.addAttribute("username", username);
        model.addAttribute("roles", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return "hello";
    }
}
