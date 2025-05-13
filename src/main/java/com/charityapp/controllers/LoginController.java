package com.charityapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        logger.info("Accès à la page de connexion");
        if (error != null) {
            model.addAttribute("error", "Email ou mot de passe incorrect");
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin() {
        logger.info("Tentative de connexion");
        return "redirect:/";
    }
} 