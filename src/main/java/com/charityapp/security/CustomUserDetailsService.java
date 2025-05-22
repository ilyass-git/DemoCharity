package com.charityapp.security;

import com.charityapp.entities.Utilisateur;
import com.charityapp.repositories.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UtilisateurRepository utilisateurRepository;

    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Tentative de chargement de l'utilisateur avec l'email: {}", email);
        
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Utilisateur non trouvé avec l'email: {}", email);
                    return new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
                });
        
        logger.info("Utilisateur trouvé: {}", utilisateur.getEmail());
        logger.info("Mot de passe stocké: {}", utilisateur.getMotDePasse());
        logger.info("Rôles de l'utilisateur: {}", utilisateur.getRoles());
        
        List<SimpleGrantedAuthority> authorities = utilisateur.getRoles().stream()
                .map(role -> {
                    logger.info("Traitement du rôle: {}", role);
                    return new SimpleGrantedAuthority(role);
                })
                .collect(Collectors.toList());
        
        logger.info("Authorities générées: {}", authorities);
        
        UserDetails userDetails = User.builder()
                .username(utilisateur.getEmail())
                .password(utilisateur.getMotDePasse())
                .authorities(authorities)
                .build();
        
        logger.info("UserDetails créé avec succès pour l'utilisateur: {}", email);
        return userDetails;
    }
} 