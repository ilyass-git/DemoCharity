package com.charityapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterTypeController {
    
    private static final Logger logger = LoggerFactory.getLogger(RegisterTypeController.class);

    @GetMapping("/register-type")
    public String registerType() {
        logger.info("Accès à la page de sélection du type d'inscription");
        return "register-type";
    }

    @GetMapping("/donateur/register")
    public String donateurRegister() {
        logger.info("Accès à la page d'inscription donateur");
        return "register";
    }
} 