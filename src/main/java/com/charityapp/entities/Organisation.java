package com.charityapp.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

/**
 * Entité Organisation représentant une organisation caritative
 * Cette classe gère les informations d'une organisation et son statut
 * Elle est liée à un administrateur (Utilisateur) et à ses actions caritatives
 */
@Entity
@Table(name = "organisation")
@Data
public class Organisation {
    /**
     * Identifiant unique de l'organisation
     * Généré automatiquement par la base de données
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom de l'organisation
     * Champ obligatoire et unique
     */
    @Column(nullable = false)
    private String nom;

    /**
     * Adresse physique de l'organisation
     * Utilisée pour la localisation
     */
    @Column(nullable = false)
    private String adresse;

    /**
     * Ville où se trouve l'organisation
     * Utilisée pour le filtrage géographique
     */
    @Column(nullable = false)
    private String ville;

    /**
     * Pays de l'organisation
     * Permet l'internationalisation
     */
    @Column(nullable = false)
    private String pays;

    /**
     * Code postal de l'organisation
     * Utilisé pour la localisation précise
     */
    @Column(name = "code_postal", nullable = false)
    private String codePostal;

    /**
     * Email de contact de l'organisation
     * Utilisé pour la communication
     */
    @Column(nullable = false)
    private String email;

    /**
     * Numéro de téléphone de l'organisation
     * Utilisé pour le contact direct
     */
    @Column(nullable = false)
    private String telephone;

    /**
     * Description détaillée de l'organisation
     * Permet de présenter les activités et objectifs
     */
    @Column
    private String description;

    /**
     * URL du logo de l'organisation
     * Stocké en externe pour optimiser la base de données
     */
    @Column(name = "logo_url")
    private String logoUrl;

    /**
     * Statut actuel de l'organisation
     * EN_ATTENTE : En attente de validation
     * VALIDEE : Organisation validée et active
     * REJETEE : Organisation rejetée
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutOrganisation statut = StatutOrganisation.EN_ATTENTE;

    /**
     * Administrateur de l'organisation
     * Relation Many-to-One avec Utilisateur
     * Un admin peut gérer plusieurs organisations
     */
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Utilisateur admin;

    /**
     * Liste des actions caritatives de l'organisation
     * Relation One-to-Many avec ActionDeCharite
     * Une organisation peut avoir plusieurs actions
     */
    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL)
    private List<ActionDeCharite> actionsDeCharite;
} 