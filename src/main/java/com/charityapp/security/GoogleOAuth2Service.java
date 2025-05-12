package com.charityapp.security;

import com.charityapp.entities.Utilisateur;
import com.charityapp.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleOAuth2Service extends DefaultOAuth2UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(GoogleOAuth2Service.class);
    
    private final UtilisateurRepository utilisateurRepository;
    private final JwtService jwtService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        try {
            return processOAuth2User(oauth2User);
        } catch (Exception ex) {
            logger.error("Erreur lors du traitement de l'utilisateur OAuth2", ex);
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String[] nameParts = name.split(" ", 2);
        String prenom = nameParts[0];
        String nom = nameParts.length > 1 ? nameParts[1] : "";
        String picture = (String) attributes.get("picture");

        Optional<Utilisateur> userOptional = utilisateurRepository.findByEmail(email);
        
        if (userOptional.isPresent()) {
            logger.info("Utilisateur existant connecté via Google: {}", email);
            Utilisateur user = userOptional.get();
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setPhoto(picture);
            return new CustomOAuth2User(user, attributes);
        } else {
            logger.info("Création d'un nouvel utilisateur via Google: {}", email);
            Utilisateur newUser = new Utilisateur();
            newUser.setEmail(email);
            newUser.setPrenom(prenom);
            newUser.setNom(nom);
            newUser.setMotDePasse(""); // Les utilisateurs OAuth2 n'ont pas de mot de passe
            newUser.setRoles(Collections.singletonList("ROLE_USER"));
            newUser.setPhoto(picture);
            newUser.setNotificationsEmailActivees(true);
            
            Utilisateur savedUser = utilisateurRepository.save(newUser);
            return new CustomOAuth2User(savedUser, attributes);
        }
    }

    private static class CustomOAuth2User implements OAuth2User {
        private final Utilisateur utilisateur;
        private final Map<String, Object> attributes;

        public CustomOAuth2User(Utilisateur utilisateur, Map<String, Object> attributes) {
            this.utilisateur = utilisateur;
            this.attributes = attributes;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return attributes;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        @Override
        public String getName() {
            return utilisateur.getNom() + " " + utilisateur.getPrenom();
        }

        public Utilisateur getUtilisateur() {
            return utilisateur;
        }
    }
} 