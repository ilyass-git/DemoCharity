package com.charityapp.controllers;

import com.charityapp.entities.Utilisateur;
import com.charityapp.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {
    
    private final UtilisateurService utilisateurService;
    
    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }
    
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);
        System.out.println("Principal: " + authentication.getPrincipal());
        System.out.println("Authorities: " + authentication.getAuthorities());
        
        String email = authentication.getName();
        System.out.println("Email: " + email);
        
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);
        System.out.println("Utilisateur trouvé: " + utilisateur);
        
        if (utilisateur != null) {
            System.out.println("Rôles de l'utilisateur: " + utilisateur.getRoles());
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", utilisateur.getId());
            userInfo.put("email", utilisateur.getEmail());
            userInfo.put("nom", utilisateur.getNom());
            userInfo.put("prenom", utilisateur.getPrenom());
            userInfo.put("roles", new ArrayList<>(utilisateur.getRoles()));
            
            System.out.println("Réponse JSON: " + userInfo);
            return ResponseEntity.ok(userInfo);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);
        System.out.println("Principal: " + authentication.getPrincipal());
        System.out.println("Authorities: " + authentication.getAuthorities());
        
        String email = authentication.getName();
        System.out.println("Email: " + email);
        
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);
        System.out.println("Utilisateur trouvé: " + utilisateur);
        
        if (utilisateur != null) {
            System.out.println("Rôles de l'utilisateur: " + utilisateur.getRoles());
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", utilisateur.getId());
            userInfo.put("email", utilisateur.getEmail());
            userInfo.put("nom", utilisateur.getNom());
            userInfo.put("prenom", utilisateur.getPrenom());
            userInfo.put("roles", utilisateur.getRoles());
            
            return ResponseEntity.ok(userInfo);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        try {
            Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
            return ResponseEntity.ok(utilisateur);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.saveUtilisateur(utilisateur));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        try {
            utilisateur.setId(id);
            return ResponseEntity.ok(utilisateurService.saveUtilisateur(utilisateur));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        try {
            utilisateurService.deleteUtilisateur(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 