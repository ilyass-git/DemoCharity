package com.charityapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String nif; // Numéro d'identification fiscale

    @Column(nullable = false)
    private String adresse;

    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private String pays;

    @Column(nullable = false)
    private String codePostal;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telephone;

    @Column(length = 1000)
    private String description;

    @Column
    private String logoUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutOrganisation statut = StatutOrganisation.EN_ATTENTE;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Utilisateur admin;

    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ActionDeCharite> actionsDeCharite;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;

    @ManyToOne
    @JoinColumn(name = "validateur_id")
    private Utilisateur validateur;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValidation;
} 