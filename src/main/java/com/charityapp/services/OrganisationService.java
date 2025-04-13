package com.charityapp.services;

import com.charityapp.entities.Organisation;
import com.charityapp.repositories.OrganisationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrganisationService {
    
    private final OrganisationRepository organisationRepository;
    
    public OrganisationService(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }
    
    public List<Organisation> getAllOrganisations() {
        return organisationRepository.findAll();
    }
    
    public Organisation getOrganisationById(Long id) {
        return organisationRepository.findById(id).orElse(null);
    }
    
    public Organisation saveOrganisation(Organisation organisation) {
        return organisationRepository.save(organisation);
    }
    
    public void deleteOrganisation(Long id) {
        organisationRepository.deleteById(id);
    }
} 