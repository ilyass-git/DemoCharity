package com.charityapp.security;

import com.charityapp.entities.Utilisateur;
import com.charityapp.repositories.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        logger.info("Début de l'inscription pour l'email: {}", request.getEmail());
        
        // Vérifier si l'utilisateur existe déjà
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.warn("Tentative d'inscription avec un email déjà utilisé: {}", request.getEmail());
            throw new RuntimeException("Cet email est déjà utilisé");
        }
        
        var user = new Utilisateur();
        user.setPrenom(request.getPrenom());
        user.setNom(request.getNom());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNumeroTelephone(request.getNumeroTelephone());
        user.setAdresse(request.getAdresse());
        user.setVille(request.getVille());
        user.setPays(request.getPays());
        user.setCodePostal(request.getCodePostal());
        user.setLangue(request.getLangue());
        user.setNotificationsEmailActivees(true);
        
        // Ajouter un rôle par défaut
        user.setRoles(Arrays.asList("ROLE_USER"));
        
        logger.info("Sauvegarde de l'utilisateur: {}", user.getEmail());
        utilisateurRepository.save(user);
        
        var jwtToken = jwtService.generateToken(user);
        logger.info("Token JWT généré pour l'utilisateur: {}", user.getEmail());
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        logger.info("Début de l'authentification pour l'email: {}", request.getEmail());
        
        try {
            logger.info("Tentative d'authentification avec l'email: {}", request.getEmail());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            logger.info("Authentification réussie pour l'email: {}", request.getEmail());
        } catch (Exception e) {
            logger.error("Erreur d'authentification pour l'email: {} - {}", request.getEmail(), e.getMessage());
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
        
        var user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logger.error("Utilisateur non trouvé pour l'email: {}", request.getEmail());
                    return new RuntimeException("Utilisateur non trouvé");
                });
        
        var jwtToken = jwtService.generateToken(user);
        logger.info("Token JWT généré pour l'utilisateur: {}", user.getEmail());
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
} 