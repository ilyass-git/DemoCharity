package com.charityapp.controllers;

import com.charityapp.services.IOrganisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;
import java.util.stream.Collectors;

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
    public String validationPage(Model model, HttpServletRequest request) {
        logger.info("=== Accès à la page de validation des organisations ===");
        logger.info("URI: {}", request.getRequestURI());
        logger.info("Méthode: {}", request.getMethod());
        logger.info("Headers: {}", Collections.list(request.getHeaderNames()).stream()
            .collect(Collectors.toMap(
                headerName -> headerName,
                request::getHeader
            )));
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authentification actuelle: {}", auth);
        if (auth != null) {
            logger.info("Principal: {}", auth.getPrincipal());
            logger.info("Authorities: {}", auth.getAuthorities());
        }
        
        try {
            logger.info("Chargement des organisations en attente...");
            model.addAttribute("organisations", organisationService.getOrganisationsEnAttente());
            logger.info("Organisations chargées avec succès");
            return "admin-validation";
        } catch (Exception e) {
            logger.error("Erreur lors du chargement des organisations en attente: {}", e.getMessage(), e);
            return "error";
        }
    }
} 