package com.charityapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrganisationRegisterController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrganisationRegisterController.class);

    @GetMapping("/organisation/register")
    public String organisationRegister() {
        logger.info("Accès à la page d'inscription organisation");
        return "organisation-register";
    }
} 