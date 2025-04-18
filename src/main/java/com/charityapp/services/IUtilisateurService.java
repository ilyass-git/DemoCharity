package com.charityapp.services;

import com.charityapp.entities.Utilisateur;
import java.util.List;

/**
 * Interface pour le service de gestion des utilisateurs.
 * Cette interface définit les opérations disponibles sur les utilisateurs.
 */
public interface IUtilisateurService {
    
    /**
     * Récupère tous les utilisateurs.
     * @return Liste de tous les utilisateurs
     */
    List<Utilisateur> getAllUtilisateurs();
    
    /**
     * Récupère un utilisateur par son ID.
     * @param id ID de l'utilisateur
     * @return L'utilisateur correspondant ou null si non trouvé
     */
    Utilisateur getUtilisateurById(Long id);
    
    /**
     * Sauvegarde un utilisateur.
     * @param utilisateur L'utilisateur à sauvegarder
     * @return L'utilisateur sauvegardé
     */
    Utilisateur saveUtilisateur(Utilisateur utilisateur);
    
    /**
     * Supprime un utilisateur par son ID.
     * @param id ID de l'utilisateur à supprimer
     */
    void deleteUtilisateur(Long id);
    
    /**
     * Récupère un utilisateur par son email.
     * @param email Email de l'utilisateur
     * @return L'utilisateur correspondant ou null si non trouvé
     */
    Utilisateur getUtilisateurByEmail(String email);
} 