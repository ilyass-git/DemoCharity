package com.charityapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

/**
 * Entité Utilisateur représentant tous les types d'utilisateurs du système
 * Cette classe gère les informations d'authentification et les rôles des utilisateurs
 * Elle est liée aux organisations (pour les admins) et aux dons (pour les donateurs)
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "utilisateur")
public class Utilisateur implements UserDetails {
    /**
     * Identifiant unique de l'utilisateur
     * Généré automatiquement par la base de données
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    /**
     * Email de l'utilisateur, utilisé comme identifiant de connexion
     * Doit être unique dans le système
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Mot de passe hashé de l'utilisateur
     * Ne doit jamais être stocké en clair
     */
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column(name = "numero_telephone")
    private String numeroTelephone;

    private String adresse;
    private String ville;
    private String pays;
    private String codePostal;

    @Column(nullable = false)
    private String langue = "fr";

    @Column(name = "notifications_email_activees")
    private boolean notificationsEmailActivees = true;

    @Column
    private String photo; // URL de la photo de profil

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    /**
     * Liste des organisations gérées par cet utilisateur
     * Relation bidirectionnelle avec Organisation
     * Un utilisateur peut gérer plusieurs organisations
     */
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Don> dons;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Organisation> organisations;

    @ManyToMany
    @JoinTable(
        name = "utilisateur_interets",
        joinColumns = @JoinColumn(name = "utilisateur_id"),
        inverseJoinColumns = @JoinColumn(name = "categorie_id")
    )
    private List<Categorie> interets;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "utilisateur_roles", joinColumns = @JoinColumn(name = "utilisateur_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    public void removeRole(String role) {
        if (this.roles != null) {
            this.roles.remove(role);
        }
    }

    public boolean hasRole(String role) {
        return this.roles != null && this.roles.contains(role);
    }
} 