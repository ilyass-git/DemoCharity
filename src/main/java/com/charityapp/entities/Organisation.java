package com.charityapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    @Column
    private String adresseLegale;

    @Column(unique = true)
    private String numeroIdentificationFiscale;

    @Column
    private String nomContactPrincipal;

    @Column
    private String emailContactPrincipal;

    @Column
    private String telephoneContactPrincipal;

    @Column
    private String urlLogo;

    @Column(length = 1000)
    private String descriptionMission;

    @Column(nullable = false)
    private boolean estApprouvee = false;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;

    @OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("organisation")
    private List<ActionDeCharite> actionsDeCharite;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Utilisateur admin;

    @PrePersist
    protected void onCreate() {
        dateCreation = new Date();
        dateModification = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = new Date();
    }
} 