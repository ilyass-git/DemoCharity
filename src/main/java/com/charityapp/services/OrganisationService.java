package com.charityapp.services;

import com.charityapp.entities.Organisation;
import com.charityapp.entities.StatutOrganisation;
import com.charityapp.entities.Utilisateur;
import com.charityapp.repositories.OrganisationRepository;
import com.charityapp.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganisationService implements IOrganisationService {
    private final OrganisationRepository organisationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrganisationService.class);
    
    @Override
    public List<Organisation> getAllOrganisations() {
        logger.info("Récupération de toutes les organisations");
        List<Organisation> organisations = organisationRepository.findAll();
        logger.info("Nombre d'organisations trouvées : {}", organisations.size());
        organisations.forEach(org -> logger.info("Organisation : {} (ID: {}, Statut: {})", org.getNom(), org.getId(), org.getStatut()));
        return organisations;
    }
    
    @Override
    public Organisation getOrganisationById(Long id) {
        return organisationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisation non trouvée"));
    }
    
    @Override
    public Organisation saveOrganisation(Organisation organisation) {
        return organisationRepository.save(organisation);
    }
    
    @Override
    public void deleteOrganisation(Long id) {
        organisationRepository.deleteById(id);
    }
    
    @Override
    public Organisation getOrganisationByNom(String nom) {
        return organisationRepository.findByNom(nom)
                .orElseThrow(() -> new RuntimeException("Organisation non trouvée"));
    }

    @Transactional
    public Organisation creerOrganisation(Organisation organisation, Long adminId) {
        logger.info("Début de la création de l'organisation: {}", organisation.getNom());
        logger.info("Admin ID: {}", adminId);
        
        try {
            // Vérification de l'existence de l'admin
        Utilisateur admin = utilisateurRepository.findById(adminId)
                .orElseThrow(() -> {
                    logger.error("Admin non trouvé avec l'ID: {}", adminId);
                    return new RuntimeException("Admin non trouvé");
                });
        
            // Vérification de l'unicité de l'email
            if (organisationRepository.findByEmail(organisation.getEmail()).isPresent()) {
                logger.error("Email déjà utilisé: {}", organisation.getEmail());
                throw new RuntimeException("Cet email est déjà utilisé par une autre organisation");
            }
            
            // Configuration de l'organisation
        organisation.setAdmin(admin);
        organisation.setStatut(StatutOrganisation.EN_ATTENTE);
            
            // Sauvegarde de l'organisation
            Organisation savedOrganisation = organisationRepository.save(organisation);
            logger.info("Organisation créée avec succès: {}", savedOrganisation.getNom());
            
            return savedOrganisation;
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'organisation: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la création de l'organisation: " + e.getMessage());
        }
    }

    @Transactional
    public Organisation validerOrganisation(Long organisationId, Long validateurId) {
        Organisation organisation = organisationRepository.findById(organisationId)
            .orElseThrow(() -> new RuntimeException("Organisation non trouvée"));
        
        Utilisateur validateur = utilisateurRepository.findById(validateurId)
            .orElseThrow(() -> new RuntimeException("Validateur non trouvé"));
        
        organisation.setStatut(StatutOrganisation.VALIDEE);
        
        return organisationRepository.save(organisation);
    }

    @Transactional
    public Organisation rejeterOrganisation(Long organisationId, Long validateurId) {
        Organisation organisation = organisationRepository.findById(organisationId)
            .orElseThrow(() -> new RuntimeException("Organisation non trouvée"));
        
        Utilisateur validateur = utilisateurRepository.findById(validateurId)
            .orElseThrow(() -> new RuntimeException("Validateur non trouvé"));
        
        organisation.setStatut(StatutOrganisation.REJETEE);
        
        return organisationRepository.save(organisation);
    }
    
    @Override
    public List<Organisation> getOrganisationsEnAttente() {
        logger.info("=== Récupération des organisations en attente ===");
        List<Organisation> organisations = organisationRepository.findByStatut(StatutOrganisation.EN_ATTENTE);
        logger.info("Nombre d'organisations en attente trouvées : {}", organisations.size());
        organisations.forEach(org -> logger.info("Organisation en attente : {} (ID: {}, Statut: {})", org.getNom(), org.getId(), org.getStatut()));
        return organisations;
    }

    public List<Organisation> getOrganisationsValidees() {
        List<Organisation> organisations = organisationRepository.findByStatut(StatutOrganisation.VALIDEE);
        logger.info("Nombre d'organisations validées trouvées : {}", organisations.size());
        organisations.forEach(org -> logger.info("Organisation validée : {} (ID: {})", org.getNom(), org.getId()));
        return organisations;
    }

    public List<Organisation> getOrganisationsByAdmin(Long adminId) {
        return organisationRepository.findByAdminId(adminId);
    }

    @Transactional
    public Organisation mettreAJourOrganisation(Long id, Organisation organisationDetails) {
        Organisation organisation = organisationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Organisation non trouvée"));
        
        organisation.setNom(organisationDetails.getNom());
        organisation.setAdresse(organisationDetails.getAdresse());
        organisation.setVille(organisationDetails.getVille());
        organisation.setPays(organisationDetails.getPays());
        organisation.setCodePostal(organisationDetails.getCodePostal());
        organisation.setTelephone(organisationDetails.getTelephone());
        organisation.setDescription(organisationDetails.getDescription());
        organisation.setLogoUrl(organisationDetails.getLogoUrl());
        
        return organisationRepository.save(organisation);
    }
} 