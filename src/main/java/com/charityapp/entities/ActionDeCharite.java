package com.charityapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionDeCharite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String titre;

    @Column(length = 2000)
    private String description;

    @Column
    private Date dateDebut;

    @Column
    private Date dateFin;

    @Column
    private String lieu;

    @Column
    private BigDecimal objectifCollecte;

    @Column
    private BigDecimal montantActuel = BigDecimal.ZERO;

    @Column
    private boolean estArchivee = false;

    @Column
    private Date dateCreation;

    @Column
    private Date dateModification;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organisation_id")
    @JsonIgnoreProperties({"actionsDeCharite", "admin"})
    private Organisation organisation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorie_id")
    @JsonIgnoreProperties("actionsDeCharite")
    private Categorie categorie;

    @OneToMany(mappedBy = "actionDeCharite", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("actionDeCharite")
    private List<Don> dons;

    @OneToMany(mappedBy = "actionDeCharite", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("actionDeCharite")
    private List<Media> medias;

    @PrePersist
    protected void onCreate() {
        dateCreation = new Date();
        dateModification = new Date();
        if (montantActuel == null) {
            montantActuel = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = new Date();
    }
} 