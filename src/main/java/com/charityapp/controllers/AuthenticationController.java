package com.charityapp.controllers;

import com.charityapp.security.AuthenticationRequest;
import com.charityapp.security.AuthenticationResponse;
import com.charityapp.security.AuthenticationService;
import com.charityapp.security.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
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
            if (request.getEmail() == null || request.getPassword() == null) {
                logger.error("Email ou mot de passe manquant");
                return ResponseEntity.badRequest().body("Email et mot de passe sont requis");
            }

            AuthenticationResponse response = authenticationService.register(request);
            logger.info("Inscription réussie pour l'email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de l'inscription: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        logger.info("Tentative de connexion pour l'email: {}", request.getEmail());
        try {
            // Validation des champs requis
            if (request.getEmail() == null || request.getPassword() == null) {
                logger.error("Email ou mot de passe manquant");
                return ResponseEntity.badRequest().body("Email et mot de passe sont requis");
            }

            AuthenticationResponse response = authenticationService.authenticate(request);
            logger.info("Connexion réussie pour l'email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de la connexion: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 