package com.charityapp.controllers;

import com.charityapp.services.IOrganisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminValidationController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminValidationController.class);
    private final IOrganisationService organisationService;

    public AdminValidationController(IOrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    @GetMapping("/validation")
    public String validationPage(Model model) {
        logger.info("Accès à la page de validation des organisations");
        try {
            model.addAttribute("organisations", organisationService.getOrganisationsEnAttente());
            return "admin-validation";
        } catch (Exception e) {
            logger.error("Erreur lors du chargement des organisations en attente: {}", e.getMessage());
            return "error";
        }
    }
} 