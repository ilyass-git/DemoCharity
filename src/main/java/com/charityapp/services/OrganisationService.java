package com.charityapp.services;

import com.charityapp.entities.Organisation;
import com.charityapp.entities.StatutOrganisation;
import com.charityapp.entities.Utilisateur;
import com.charityapp.repositories.OrganisationRepository;
import com.charityapp.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
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

    @Override
    public List<Organisation> getAllOrganisations() {
        return organisationRepository.findAll();
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
        Utilisateur admin = utilisateurRepository.findById(adminId)
            .orElseThrow(() -> new RuntimeException("Admin non trouvé"));
        
        organisation.setAdmin(admin);
        organisation.setStatut(StatutOrganisation.EN_ATTENTE);
        return organisationRepository.save(organisation);
    }

    @Transactional
    public Organisation validerOrganisation(Long organisationId, Long validateurId) {
        Organisation organisation = organisationRepository.findById(organisationId)
            .orElseThrow(() -> new RuntimeException("Organisation non trouvée"));
        
        Utilisateur validateur = utilisateurRepository.findById(validateurId)
            .orElseThrow(() -> new RuntimeException("Validateur non trouvé"));
        
        organisation.setStatut(StatutOrganisation.VALIDEE);
        organisation.setValidateur(validateur);
        organisation.setDateValidation(new Date());
        
        return organisationRepository.save(organisation);
    }

    @Transactional
    public Organisation rejeterOrganisation(Long organisationId, Long validateurId) {
        Organisation organisation = organisationRepository.findById(organisationId)
            .orElseThrow(() -> new RuntimeException("Organisation non trouvée"));
        
        Utilisateur validateur = utilisateurRepository.findById(validateurId)
            .orElseThrow(() -> new RuntimeException("Validateur non trouvé"));
        
        organisation.setStatut(StatutOrganisation.REJETEE);
        organisation.setValidateur(validateur);
        organisation.setDateValidation(new Date());
        
        return organisationRepository.save(organisation);
    }

    @Override
    public List<Organisation> getOrganisationsEnAttente() {
        return organisationRepository.findByStatut(StatutOrganisation.EN_ATTENTE);
    }

    public List<Organisation> getOrganisationsValidees() {
        return organisationRepository.findByStatut(StatutOrganisation.VALIDEE);
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