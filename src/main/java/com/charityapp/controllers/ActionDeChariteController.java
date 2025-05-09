package com.charityapp.controllers;

import com.charityapp.dto.ActionChariteDTO;
import com.charityapp.entities.ActionDeCharite;
import com.charityapp.entities.Categorie;
import com.charityapp.entities.Media;
import com.charityapp.services.IActionDeChariteService;
import com.charityapp.services.ICategorieService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActionDeChariteController {
    
    private static final Logger logger = LoggerFactory.getLogger(ActionDeChariteController.class);
    
    private final IActionDeChariteService actionDeChariteService;
    private final ICategorieService categorieService;

    @GetMapping("/public")
    public ResponseEntity<List<ActionDeCharite>> getPublicActions() {
        logger.info("Récupération des actions de charité publiques");
        return ResponseEntity.ok(actionDeChariteService.getAllActions());
    }

    @GetMapping
    public ResponseEntity<List<ActionDeCharite>> getAllActions() {
        logger.info("Début de la récupération de toutes les actions de charité");
        try {
            List<ActionDeCharite> actions = actionDeChariteService.getAllActions();
            logger.info("Actions récupérées avec succès. Nombre d'actions: {}", actions.size());
            return ResponseEntity.ok(actions);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des actions: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionDeCharite> getActionById(@PathVariable Long id) {
        return actionDeChariteService.getActionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAction(@RequestBody ActionChariteDTO dto) {
        logger.info("Création d'une nouvelle action de charité: {}", dto.getTitre());
        try {
            // Vérifier si la catégorie existe
            Categorie categorie = categorieService.getCategorieById(dto.getCategorieId());
            if (categorie == null) {
                logger.warn("Catégorie non trouvée avec l'ID: {}", dto.getCategorieId());
                return ResponseEntity.badRequest().body("Catégorie non trouvée");
            }

            // Créer une nouvelle action
            ActionDeCharite action = new ActionDeCharite();
            action.setTitre(dto.getTitre());
            action.setDescription(dto.getDescription());
            action.setDateDebut(Date.from(dto.getDateDebut().atZone(ZoneId.systemDefault()).toInstant()));
            action.setDateFin(Date.from(dto.getDateFin().atZone(ZoneId.systemDefault()).toInstant()));
            action.setLieu(dto.getLieu());
            action.setObjectifCollecte(dto.getObjectifCollecte());
            action.setCategorie(categorie);

            // Sauvegarder l'action
            ActionDeCharite savedAction = actionDeChariteService.saveAction(action);
            logger.info("Action de charité créée avec succès: {}", savedAction.getId());
            
            return ResponseEntity.ok(savedAction);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'action de charité: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erreur lors de la création de l'action: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionDeCharite> mettreAJourAction(
            @PathVariable Long id,
            @RequestBody ActionDeCharite actionDetails) {
        return actionDeChariteService.getActionById(id)
                .map(existingAction -> {
                    ActionDeCharite updatedAction = actionDeChariteService.mettreAJourAction(id, actionDetails);
            return ResponseEntity.ok(updatedAction);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAction(@PathVariable Long id) {
        logger.info("Suppression de l'action de charité avec l'ID: {}", id);
        try {
            return actionDeChariteService.getActionById(id)
                    .map(action -> {
            actionDeChariteService.deleteAction(id);
            logger.info("Action de charité supprimée avec succès: {}", id);
            return ResponseEntity.ok().build();
                    })
                    .orElseGet(() -> {
                        logger.warn("Action de charité non trouvée avec l'ID: {}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'action de charité: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erreur lors de la suppression de l'action: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/media")
    public ResponseEntity<ActionDeCharite> ajouterMedia(
            @PathVariable Long id,
            @RequestBody Media media) {
        return actionDeChariteService.getActionById(id)
                .map(existingAction -> {
                    ActionDeCharite updatedAction = actionDeChariteService.ajouterMedia(id, media);
                    return ResponseEntity.ok(updatedAction);
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 