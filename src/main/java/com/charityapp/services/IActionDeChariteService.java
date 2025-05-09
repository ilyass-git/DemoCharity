package com.charityapp.services;

import com.charityapp.entities.ActionDeCharite;
import com.charityapp.entities.Media;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour le service de gestion des actions de charité.
 * Cette interface définit les opérations disponibles sur les actions de charité.
 */
public interface IActionDeChariteService {
    
    /**
     * Récupère toutes les actions de charité.
     * @return Liste de toutes les actions de charité
     */
    List<ActionDeCharite> getAllActions();
    
    /**
     * Récupère une action de charité par son ID.
     * @param id ID de l'action de charité
     * @return L'action de charité correspondante ou null si non trouvée
     */
    Optional<ActionDeCharite> getActionById(Long id);
    
    /**
     * Sauvegarde une action de charité.
     * @param action L'action de charité à sauvegarder
     * @return L'action de charité sauvegardée
     */
    ActionDeCharite saveAction(ActionDeCharite action);
    
    /**
     * Supprime une action de charité par son ID.
     * @param id ID de l'action de charité à supprimer
     */
    void deleteAction(Long id);
    
    /**
     * Récupère les actions de charité par catégorie.
     * @param categorieId ID de la catégorie
     * @return Liste des actions de charité de la catégorie
     */
    List<ActionDeCharite> getActionsByCategorie(Long categorieId);
    
    /**
     * Récupère les actions de charité par organisation.
     * @param organisationId ID de l'organisation
     * @return Liste des actions de charité de l'organisation
     */
    List<ActionDeCharite> getActionsByOrganisation(Long organisationId);
    
    /**
     * Récupère les actions de charité en cours.
     * @return Liste des actions de charité en cours
     */
    List<ActionDeCharite> getActionsEnCours();
    
    /**
     * Récupère les actions de charité terminées.
     * @return Liste des actions de charité terminées
     */
    List<ActionDeCharite> getActionsTerminees();

    /**
     * Crée une nouvelle action de charité pour une organisation.
     * @param organisationId ID de l'organisation
     * @param action L'action de charité à créer
     * @return L'action de charité créée
     */
    ActionDeCharite creerAction(Long organisationId, ActionDeCharite action);

    /**
     * Ajoute un média à une action de charité.
     * @param actionId ID de l'action de charité
     * @param media Le média à ajouter
     * @return L'action de charité mise à jour
     */
    ActionDeCharite ajouterMedia(Long actionId, Media media);

    /**
     * Archive une action de charité.
     * @param actionId ID de l'action de charité
     * @return L'action de charité archivée
     */
    ActionDeCharite archiverAction(Long actionId);

    /**
     * Met à jour une action de charité.
     * @param actionId ID de l'action de charité
     * @param actionDetails Les nouvelles informations de l'action
     * @return L'action de charité mise à jour
     */
    ActionDeCharite mettreAJourAction(Long actionId, ActionDeCharite actionDetails);
} 