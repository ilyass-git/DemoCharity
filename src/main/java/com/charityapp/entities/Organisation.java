package com.charityapp.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "organisation")
@Data
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String adresse;

    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private String pays;

    @Column(name = "code_postal", nullable = false)
    private String codePostal;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telephone;

    @Column
    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutOrganisation statut = StatutOrganisation.EN_ATTENTE;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Utilisateur admin;

    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL)
    private List<ActionDeCharite> actionsDeCharite;
} 