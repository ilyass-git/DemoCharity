package com.charityapp.services;

import com.charityapp.entities.Categorie;
import java.util.List;

/**
 * Interface pour le service de gestion des catégories.
 * Cette interface définit les opérations disponibles sur les catégories.
 */
public interface ICategorieService {
    
    /**
     * Récupère toutes les catégories.
     * @return Liste de toutes les catégories
     */
    List<Categorie> getAllCategories();
    
    /**
     * Récupère une catégorie par son ID.
     * @param id ID de la catégorie
     * @return La catégorie correspondante ou null si non trouvée
     */
    Categorie getCategorieById(Long id);
    
    /**
     * Sauvegarde une catégorie.
     * @param categorie La catégorie à sauvegarder
     * @return La catégorie sauvegardée
     */
    Categorie saveCategorie(Categorie categorie);
    
    /**
     * Supprime une catégorie par son ID.
     * @param id ID de la catégorie à supprimer
     */
    void deleteCategorie(Long id);
    
    /**
     * Récupère une catégorie par son nom.
     * @param nom Nom de la catégorie
     * @return La catégorie correspondante ou null si non trouvée
     */
    Categorie getCategorieByNom(String nom);
} 