package com.charityapp.controllers;

import com.charityapp.security.AuthenticationRequest;
import com.charityapp.security.AuthenticationResponse;
import com.charityapp.security.AuthenticationService;
import com.charityapp.security.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.info("Tentative d'inscription pour l'email: {}", request.getEmail());
        try {
            // Validation des champs requis
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                logger.error("Email manquant");
                return ResponseEntity.badRequest().body("L'email est requis");
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                logger.error("Mot de passe manquant");
                return ResponseEntity.badRequest().body("Le mot de passe est requis");
            }
            
            if (request.getPrenom() == null || request.getPrenom().trim().isEmpty()) {
                logger.error("Prénom manquant");
                return ResponseEntity.badRequest().body("Le prénom est requis");
            }
            
            if (request.getNom() == null || request.getNom().trim().isEmpty()) {
                logger.error("Nom manquant");
                return ResponseEntity.badRequest().body("Le nom est requis");
            }

            AuthenticationResponse response = authenticationService.register(request);
            logger.info("Inscription réussie pour l'email: {}", request.getEmail());
            
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", response.getToken());
            responseBody.put("id", response.getId());
            responseBody.put("message", "Inscription réussie");
            
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            logger.error("Erreur lors de l'inscription: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'inscription: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        logger.info("Tentative de connexion pour l'email: {}", request.getEmail());
        try {
            // Validation des champs requis
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                logger.error("Email manquant");
                return ResponseEntity.badRequest().body("L'email est requis");
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                logger.error("Mot de passe manquant");
                return ResponseEntity.badRequest().body("Le mot de passe est requis");
            }

            AuthenticationResponse response = authenticationService.authenticate(request);
            logger.info("Connexion réussie pour l'email: {}", request.getEmail());
            
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", response.getToken());
            responseBody.put("message", "Connexion réussie");
            
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            logger.error("Erreur lors de la connexion: {}", e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Email ou mot de passe incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
} 