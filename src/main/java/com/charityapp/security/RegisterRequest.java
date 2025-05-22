package com.charityapp.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String prenom;
    private String nom;
    private String email;
    private String password;
    private String numeroTelephone;
    private String adresse;
    private String ville;
    private String pays;
    private String codePostal;
    private String langue;
    private String role;
} 