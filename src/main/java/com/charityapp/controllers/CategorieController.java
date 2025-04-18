package com.charityapp.controllers;

import com.charityapp.entities.Categorie;
import com.charityapp.services.ICategorieService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategorieController {
    
    private static final Logger logger = LoggerFactory.getLogger(CategorieController.class);
    
    private final ICategorieService categorieService;

    @GetMapping("/public")
    public ResponseEntity<List<Categorie>> getPublicCategories() {
        logger.info("Récupération des catégories publiques");
        return ResponseEntity.ok(categorieService.getAllCategories());
    }

    @GetMapping
    public ResponseEntity<List<Categorie>> getAllCategories() {
        logger.info("Début de la récupération de toutes les catégories");
        try {
            List<Categorie> categories = categorieService.getAllCategories();
            logger.info("Catégories récupérées avec succès. Nombre de catégories: {}", categories.size());
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des catégories: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categorie> getCategorieById(@PathVariable Long id) {
        logger.info("Récupération de la catégorie avec l'ID: {}", id);
        try {
            Categorie categorie = categorieService.getCategorieById(id);
            if (categorie == null) {
                logger.warn("Catégorie non trouvée avec l'ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            logger.info("Catégorie récupérée avec succès: {}", categorie.getId());
            return ResponseEntity.ok(categorie);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la catégorie: {}", e.getMessage(), e);
            throw e;
        }
    }
} 