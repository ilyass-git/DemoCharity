package com.charityapp.controllers;

import com.charityapp.entities.Organisation;
import com.charityapp.services.IOrganisationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organisations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrganisationController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrganisationController.class);
    
    private final IOrganisationService organisationService;

    @GetMapping("/public")
    public ResponseEntity<List<Organisation>> getPublicOrganisations() {
        logger.info("Récupération des organisations publiques");
        return ResponseEntity.ok(organisationService.getAllOrganisations());
    }

    @GetMapping
    public ResponseEntity<List<Organisation>> getAllOrganisations() {
        logger.info("Début de la récupération de toutes les organisations");
        try {
            List<Organisation> organisations = organisationService.getAllOrganisations();
            logger.info("Organisations récupérées avec succès. Nombre d'organisations: {}", organisations.size());
            return ResponseEntity.ok(organisations);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des organisations: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organisation> getOrganisationById(@PathVariable Long id) {
        logger.info("Récupération de l'organisation avec l'ID: {}", id);
        try {
            Organisation organisation = organisationService.getOrganisationById(id);
            if (organisation == null) {
                logger.warn("Organisation non trouvée avec l'ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            logger.info("Organisation récupérée avec succès: {}", organisation.getId());
            return ResponseEntity.ok(organisation);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'organisation: {}", e.getMessage(), e);
            throw e;
        }
    }
} 