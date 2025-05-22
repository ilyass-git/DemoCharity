package com.charityapp.security;

import com.charityapp.entities.Utilisateur;
import com.charityapp.repositories.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
        logger.info("Données de la requête: prenom={}, nom={}, email={}", 
            request.getPrenom(), request.getNom(), request.getEmail());
        
        // Vérifier si l'utilisateur existe déjà
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.error("Tentative d'inscription avec un email déjà utilisé: {}", request.getEmail());
            throw new RuntimeException("Email déjà utilisé");
        }
        
        var user = new Utilisateur();
        user.setPrenom(request.getPrenom());
        user.setNom(request.getNom());
        user.setEmail(request.getEmail());
        user.setMotDePasse(passwordEncoder.encode(request.getPassword()));
        user.setNumeroTelephone(request.getNumeroTelephone());
        user.setAdresse(request.getAdresse());
        user.setVille(request.getVille());
        user.setPays(request.getPays());
        user.setCodePostal(request.getCodePostal());
        user.setLangue(request.getLangue());
        user.setNotificationsEmailActivees(true);
        
        // Ajouter un rôle par défaut
        user.setRoles(Arrays.asList("ROLE_USER"));
        logger.info("Rôles définis pour l'utilisateur: {}", user.getRoles());
        
        logger.info("Sauvegarde de l'utilisateur: {}", user.getEmail());
        user = utilisateurRepository.save(user); // Sauvegarder et récupérer l'utilisateur avec son ID
        logger.info("Utilisateur sauvegardé avec l'ID: {}", user.getId());
        
        var jwtToken = jwtService.generateToken(user);
        logger.info("Token JWT généré pour l'utilisateur: {}", user.getEmail());
        
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .build();
        logger.info("Réponse d'authentification créée avec token et ID: {}", user.getId());
        
        return response;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        logger.info("Début de l'authentification pour l'email: {}", request.getEmail());
        
        try {
            // Vérifier si l'utilisateur existe
            var user = utilisateurRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {
                        logger.error("Utilisateur non trouvé: {}", request.getEmail());
                        return new BadCredentialsException("Email ou mot de passe incorrect");
                    });
            
            logger.info("Utilisateur trouvé dans la base de données: {}", user.getEmail());
            logger.info("Hash du mot de passe stocké: {}", user.getMotDePasse());
            logger.info("Rôles de l'utilisateur: {}", user.getRoles());
            
            // Authentifier l'utilisateur
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            
            logger.info("Authentification réussie pour l'utilisateur: {}", user.getEmail());
            logger.info("Rôles de l'utilisateur authentifié: {}", user.getRoles());
            
            // Générer le token JWT
        var jwtToken = jwtService.generateToken(user);
        logger.info("Token JWT généré pour l'utilisateur: {}", user.getEmail());
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
        } catch (BadCredentialsException e) {
            logger.error("Erreur d'authentification pour l'email: {} - {}", request.getEmail(), e.getMessage());
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        } catch (Exception e) {
            logger.error("Erreur lors de l'authentification pour l'email: {} - {}", request.getEmail(), e.getMessage());
            throw new RuntimeException("Erreur lors de l'authentification");
        }
    }
} 