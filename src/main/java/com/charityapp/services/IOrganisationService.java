package com.charityapp.services;

import com.charityapp.entities.Organisation;
import java.util.List;

/**
 * Interface pour le service de gestion des organisations.
 * Cette interface définit les opérations disponibles sur les organisations.
 */
public interface IOrganisationService {
    
    /**
     * Récupère toutes les organisations.
     * @return Liste de toutes les organisations
     */
    List<Organisation> getAllOrganisations();
    
    /**
     * Récupère une organisation par son ID.
     * @param id ID de l'organisation
     * @return L'organisation correspondante ou null si non trouvée
     */
    Organisation getOrganisationById(Long id);
    
    /**
     * Sauvegarde une organisation.
     * @param organisation L'organisation à sauvegarder
     * @return L'organisation sauvegardée
     */
    Organisation saveOrganisation(Organisation organisation);
    
    /**
     * Supprime une organisation par son ID.
     * @param id ID de l'organisation à supprimer
     */
    void deleteOrganisation(Long id);
    
    /**
     * Récupère une organisation par son nom.
     * @param nom Nom de l'organisation
     * @return L'organisation correspondante ou null si non trouvée
     */
    Organisation getOrganisationByNom(String nom);

    /**
     * Récupère toutes les organisations en attente de validation.
     * @return Liste des organisations en attente
     */
    List<Organisation> getOrganisationsEnAttente();
} 