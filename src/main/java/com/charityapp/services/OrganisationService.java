package com.charityapp.services;

import com.charityapp.entities.Organisation;
import com.charityapp.repositories.OrganisationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganisationService implements IOrganisationService {
    
    private final OrganisationRepository organisationRepository;
    
    public OrganisationService(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }
    
    @Override
    public List<Organisation> getAllOrganisations() {
        return organisationRepository.findAll();
    }
    
    @Override
    public Organisation getOrganisationById(Long id) {
        return organisationRepository.findById(id).orElse(null);
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
        return organisationRepository.findAll().stream()
            .filter(org -> org.getNom().equalsIgnoreCase(nom))
            .findFirst()
            .orElse(null);
    }
    
    @Override
    public List<Organisation> getOrganisationsByCategorie(Long categorieId) {
        return organisationRepository.findAll().stream()
            .filter(org -> org.getActionsDeCharite().stream()
                .anyMatch(action -> action.getCategorie() != null && 
                        action.getCategorie().getId().equals(categorieId)))
            .collect(Collectors.toList());
    }
} 