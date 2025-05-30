package com.charityapp.controllers;

import com.charityapp.entities.Organisation;
import com.charityapp.services.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des organisations
 * Gère toutes les opérations CRUD sur les organisations
 * Inclut la validation et le rejet des organisations par les super admins
 */
@RestController
@RequestMapping("/api/organisations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrganisationController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrganisationController.class);
    
    private final OrganisationService organisationService;

    /**
     * Récupère toutes les organisations
     * Accessible uniquement aux super admins
     * @return Liste de toutes les organisations
     */
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Organisation>> getAllOrganisations() {
        try {
            logger.info("=== Récupération de toutes les organisations ===");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.info("Utilisateur authentifié : {}", auth.getName());
            logger.info("Rôles : {}", auth.getAuthorities());
            
            List<Organisation> organisations = organisationService.getAllOrganisations();
            logger.info("Nombre d'organisations trouvées : {}", organisations.size());
            return ResponseEntity.ok(organisations);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des organisations", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Enregistre une nouvelle organisation
     * Accessible à tous les utilisateurs
     * @param organisation Données de l'organisation
     * @param adminId ID de l'administrateur
     * @return Organisation créée
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerOrganisation(
            @RequestBody Organisation organisation,
            @RequestParam Long adminId) {
        try {
            logger.info("Tentative d'inscription d'une nouvelle organisation: {}", organisation.getNom());
            logger.info("Admin ID: {}", adminId);
            logger.info("Données de l'organisation: {}", organisation);
            
            // Validation des champs obligatoires
            if (organisation.getNom() == null || organisation.getNom().trim().isEmpty()) {
                logger.error("Le nom de l'organisation est requis");
                return ResponseEntity.badRequest().body("Le nom de l'organisation est requis");
            }
            
            if (organisation.getEmail() == null || organisation.getEmail().trim().isEmpty()) {
                logger.error("L'email de l'organisation est requis");
                return ResponseEntity.badRequest().body("L'email de l'organisation est requis");
            }
            
            if (organisation.getTelephone() == null || organisation.getTelephone().trim().isEmpty()) {
                logger.error("Le téléphone de l'organisation est requis");
                return ResponseEntity.badRequest().body("Le téléphone de l'organisation est requis");
            }
            
            Organisation savedOrganisation = organisationService.creerOrganisation(organisation, adminId);
            logger.info("Organisation créée avec succès: {}", savedOrganisation.getNom());
            return ResponseEntity.ok(savedOrganisation);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'organisation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la création de l'organisation: " + e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
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

    /**
     * Valide une organisation
     * Accessible uniquement aux super admins
     * @param id ID de l'organisation à valider
     * @param validateurId ID du super admin qui valide
     * @return Organisation validée
     */
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

    /**
     * Rejette une organisation
     * Accessible uniquement aux super admins
     * @param id ID de l'organisation à rejeter
     * @param validateurId ID du super admin qui rejette
     * @return Organisation rejetée
     */
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

    /**
     * Récupère les organisations en attente de validation
     * Accessible uniquement aux super admins
     * @return Liste des organisations en attente
     */
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping(value = "/en-attente", produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<Organisation>> getOrganisationsEnAttente() {
        try {
            logger.info("=== Récupération des organisations en attente ===");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.info("Utilisateur authentifié : {}", auth.getName());
            logger.info("Rôles : {}", auth.getAuthorities());
            
            List<Organisation> organisations = organisationService.getOrganisationsEnAttente();
            logger.info("Nombre d'organisations en attente trouvées : {}", organisations.size());
            
            // Log détaillé de chaque organisation
            organisations.forEach(org -> {
                logger.info("Organisation trouvée :");
                logger.info("  - ID : {}", org.getId());
                logger.info("  - Nom : {}", org.getNom());
                logger.info("  - Email : {}", org.getEmail());
                logger.info("  - Statut : {}", org.getStatut());
                logger.info("  - Ville : {}", org.getVille());
                logger.info("  - Pays : {}", org.getPays());
            });
            
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(organisations);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des organisations en attente", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère les organisations validées
     * Accessible à tous les utilisateurs
     * @return Liste des organisations validées
     */
    @GetMapping("/validees")
    public ResponseEntity<List<Organisation>> getOrganisationsValidees() {
        try {
            logger.info("Récupération des organisations validées");
            List<Organisation> organisations = organisationService.getOrganisationsValidees();
            logger.info("{} organisations validées trouvées", organisations.size());
            return ResponseEntity.ok(organisations);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des organisations validées", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère une organisation par son ID
     * Accessible à tous les utilisateurs authentifiés
     * @param id ID de l'organisation
     * @return Organisation trouvée
     */
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

    /**
     * Récupère les organisations d'un administrateur
     * Accessible à tous les utilisateurs authentifiés
     * @param adminId ID de l'administrateur
     * @return Liste des organisations de l'admin
     */
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