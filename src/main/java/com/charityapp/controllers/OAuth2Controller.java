package com.charityapp.controllers;

import com.charityapp.security.JwtService;
import com.charityapp.entities.Utilisateur;
import com.charityapp.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OAuth2Controller {
    private static final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);
    private final JwtService jwtService;
    private final UtilisateurRepository utilisateurRepository;

    @GetMapping("/oauth2/loginSuccess")
    @ResponseBody
    public ResponseEntity<Map<String, String>> loginSuccess(Authentication authentication) {
        logger.info("OAuth2 login success handler called");
        
        try {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            
            logger.info("OAuth2 user email: {}", email);
            
            Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseGet(() -> {
                    logger.info("Creating new user for email: {}", email);
                    Utilisateur newUser = new Utilisateur();
                    newUser.setEmail(email);
                    newUser.setNom(name);
                    newUser.setPrenom("");
                    newUser.setRoles(Arrays.asList("USER"));
                    newUser.setMotDePasse(""); // L'utilisateur se connectera uniquement via Google
                    newUser.setNumeroTelephone("");
                    newUser.setAdresse("");
                    newUser.setVille("");
                    newUser.setPays("");
                    newUser.setCodePostal("");
                    newUser.setLangue("FR");
                    return utilisateurRepository.save(newUser);
                });
            
            String token = jwtService.generateToken(utilisateur);
            logger.info("Generated JWT token for user: {}", email);
            
            return ResponseEntity.ok(Map.of(
                "token", token,
                "email", email,
                "name", utilisateur.getNom() + " " + utilisateur.getPrenom()
            ));
        } catch (Exception e) {
            logger.error("Error during OAuth2 login success handling", e);
            throw e;
        }
    }

    @GetMapping("/oauth2/loginFailure")
    public String loginFailure() {
        logger.error("OAuth2 login failure");
        return "redirect:/login?error=Google+authentication+failed";
    }
} 