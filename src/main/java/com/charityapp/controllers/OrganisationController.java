package com.charityapp.controllers;

import com.charityapp.entities.Organisation;
import com.charityapp.services.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organisations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrganisationController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrganisationController.class);
    
    private final OrganisationService organisationService;

    @GetMapping
    public ResponseEntity<List<Organisation>> getAllOrganisations() {
        try {
            List<Organisation> organisations = organisationService.getAllOrganisations();
            return ResponseEntity.ok(organisations);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des organisations", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Organisation> creerOrganisation(
            @RequestBody Organisation organisation,
            @RequestParam Long adminId) {
        try {
            return ResponseEntity.ok(organisationService.creerOrganisation(organisation, adminId));
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'organisation", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/{id}/valider")
    public ResponseEntity<Organisation> validerOrganisation(
            @PathVariable Long id,
            @RequestParam Long validateurId) {
        try {
            return ResponseEntity.ok(organisationService.validerOrganisation(id, validateurId));
        } catch (Exception e) {
            logger.error("Erreur lors de la validation de l'organisation", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/{id}/rejeter")
    public ResponseEntity<Organisation> rejeterOrganisation(
            @PathVariable Long id,
            @RequestParam Long validateurId) {
        try {
            return ResponseEntity.ok(organisationService.rejeterOrganisation(id, validateurId));
        } catch (Exception e) {
            logger.error("Erreur lors du rejet de l'organisation", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/en-attente")
    public ResponseEntity<List<Organisation>> getOrganisationsEnAttente() {
        try {
            return ResponseEntity.ok(organisationService.getOrganisationsEnAttente());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des organisations en attente", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/validees")
    public ResponseEntity<List<Organisation>> getOrganisationsValidees() {
        try {
            return ResponseEntity.ok(organisationService.getOrganisationsValidees());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des organisations validées", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organisation> getOrganisationById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(organisationService.getOrganisationById(id));
        } catch (RuntimeException e) {
            logger.error("Organisation non trouvée avec l'id: {}", id, e);
                return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'organisation", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Organisation>> getOrganisationsByAdmin(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(organisationService.getOrganisationsByAdmin(adminId));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des organisations de l'admin", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organisation> mettreAJourOrganisation(
            @PathVariable Long id,
            @RequestBody Organisation organisationDetails) {
        try {
            return ResponseEntity.ok(organisationService.mettreAJourOrganisation(id, organisationDetails));
        } catch (RuntimeException e) {
            logger.error("Organisation non trouvée avec l'id: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'organisation", e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 