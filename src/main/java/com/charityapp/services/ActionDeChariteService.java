package com.charityapp.services;

import com.charityapp.entities.ActionDeCharite;
import com.charityapp.entities.Media;
import com.charityapp.entities.Organisation;
import com.charityapp.entities.StatutOrganisation;
import com.charityapp.repositories.ActionDeChariteRepository;
import com.charityapp.repositories.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActionDeChariteService implements IActionDeChariteService {
    
    private final ActionDeChariteRepository actionDeChariteRepository;
    private final OrganisationRepository organisationRepository;
    
    @Override
    public List<ActionDeCharite> getAllActions() {
        return actionDeChariteRepository.findAll();
    }
    
    @Override
    public Optional<ActionDeCharite> getActionById(Long id) {
        return actionDeChariteRepository.findById(id);
    }
    
    @Override
    @Transactional
    public ActionDeCharite saveAction(ActionDeCharite action) {
        return actionDeChariteRepository.save(action);
    }
    
    @Override
    @Transactional
    public void deleteAction(Long id) {
        actionDeChariteRepository.deleteById(id);
    }
    
    @Override
    public List<ActionDeCharite> getActionsByCategorie(Long categorieId) {
        return actionDeChariteRepository.findAll().stream()
            .filter(action -> action.getCategorie() != null && 
                    action.getCategorie().getId().equals(categorieId))
            .toList();
    }
    
    @Override
    public List<ActionDeCharite> getActionsByOrganisation(Long organisationId) {
        return actionDeChariteRepository.findAll().stream()
            .filter(action -> action.getOrganisation() != null && 
                    action.getOrganisation().getId().equals(organisationId))
            .toList();
    }
    
    @Override
    public List<ActionDeCharite> getActionsEnCours() {
        Date now = new Date();
        return actionDeChariteRepository.findAll().stream()
            .filter(action -> !action.isEstArchivee() && 
                    action.getDateDebut().before(now) && 
                    action.getDateFin().after(now))
            .toList();
    }
    
    @Override
    public List<ActionDeCharite> getActionsTerminees() {
        Date now = new Date();
        return actionDeChariteRepository.findAll().stream()
            .filter(action -> action.isEstArchivee() || 
                    action.getDateFin().before(now))
            .toList();
    }

    @Override
    @Transactional
    public ActionDeCharite creerAction(Long organisationId, ActionDeCharite action) {
        Organisation organisation = organisationRepository.findById(organisationId)
            .orElseThrow(() -> new RuntimeException("Organisation non trouvée"));
        
        if (!organisation.getStatut().equals(StatutOrganisation.VALIDEE)) {
            throw new RuntimeException("L'organisation doit être validée pour créer des actions");
        }
        
        action.setOrganisation(organisation);
        action.setDateCreation(new Date());
        action.setDateModification(new Date());
        action.setEstArchivee(false);
        
        return actionDeChariteRepository.save(action);
    }

    @Override
    @Transactional
    public ActionDeCharite ajouterMedia(Long actionId, Media media) {
        ActionDeCharite action = actionDeChariteRepository.findById(actionId)
            .orElseThrow(() -> new RuntimeException("Action non trouvée"));
        
        action.getMedias().add(media);
        action.setDateModification(new Date());
        
        return actionDeChariteRepository.save(action);
    }

    @Override
    @Transactional
    public ActionDeCharite archiverAction(Long actionId) {
        ActionDeCharite action = actionDeChariteRepository.findById(actionId)
            .orElseThrow(() -> new RuntimeException("Action non trouvée"));
        
        action.setEstArchivee(true);
        action.setDateModification(new Date());
        
        return actionDeChariteRepository.save(action);
    }

    @Override
    @Transactional
    public ActionDeCharite mettreAJourAction(Long actionId, ActionDeCharite actionDetails) {
        ActionDeCharite action = actionDeChariteRepository.findById(actionId)
            .orElseThrow(() -> new RuntimeException("Action non trouvée"));
        
        action.setTitre(actionDetails.getTitre());
        action.setDescription(actionDetails.getDescription());
        action.setDateDebut(actionDetails.getDateDebut());
        action.setDateFin(actionDetails.getDateFin());
        action.setLieu(actionDetails.getLieu());
        action.setObjectifCollecte(actionDetails.getObjectifCollecte());
        action.setCategorie(actionDetails.getCategorie());
        action.setDateModification(new Date());
        
        return actionDeChariteRepository.save(action);
    }
} 