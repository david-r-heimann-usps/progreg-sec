package com.progreg.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.progreg.security.config.security.AuthInfo;

@Controller
public class HomeController {

    @RequestMapping( value = "/home.action", method = RequestMethod.GET )
    public String home(Model model) {
        model.addAttribute("message", "Welcome to progreg-sec");
        AuthInfo.fillModel(model);
        return "home";
    }
}
