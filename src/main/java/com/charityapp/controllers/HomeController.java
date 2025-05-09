package com.charityapp.controllers;

import com.charityapp.services.IActionDeChariteService;
import com.charityapp.services.ICategorieService;
import com.charityapp.services.IOrganisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final IActionDeChariteService actionDeChariteService;
    private final ICategorieService categorieService;
    private final IOrganisationService organisationService;

    public HomeController(IActionDeChariteService actionDeChariteService,
                         ICategorieService categorieService,
                         IOrganisationService organisationService) {
        this.actionDeChariteService = actionDeChariteService;
        this.categorieService = categorieService;
        this.organisationService = organisationService;
    }

    @GetMapping("/")
    public String home(Model model) {
        logger.info("Accès à la page d'accueil");
        try {
            model.addAttribute("actions", actionDeChariteService.getAllActions());
            model.addAttribute("categories", categorieService.getAllCategories());
            model.addAttribute("organisations", organisationService.getAllOrganisations());
            logger.info("Données chargées avec succès pour la page d'accueil");
            return "home";
        } catch (Exception e) {
            logger.error("Erreur lors du chargement des données pour la page d'accueil: {}", e.getMessage());
            return "home";
        }
    }

    @GetMapping("/login")
    public String login() {
        logger.info("Accès à la page de connexion");
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        logger.info("Redirection vers la page de sélection du type d'inscription");
        return "redirect:/register-type";
    }

    @GetMapping("/register/user")
    public String registerUser() {
        logger.info("Accès à la page d'inscription utilisateur");
        return "register";
    }
}