package com.charityapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Don {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal montant;

    @Column(nullable = false)
    private String methodePaiement; // PAYPAL ou STRIPE

    @Column(nullable = false)
    private String idTransaction;

    @Column(nullable = false)
    private Date dateDon;

    @Column(nullable = false)
    private boolean estAnonyme = false;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    @JsonIgnore
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "action_de_charite_id", nullable = false)
    private ActionDeCharite actionDeCharite;

    @PrePersist
    protected void onCreate() {
        dateDon = new Date();
    }
} 