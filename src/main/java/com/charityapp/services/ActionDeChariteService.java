package com.charityapp.services;

import com.charityapp.entities.ActionDeCharite;
import com.charityapp.repositories.ActionDeChariteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class ActionDeChariteService implements IActionDeChariteService {
    
    private final ActionDeChariteRepository actionDeChariteRepository;
    
    public ActionDeChariteService(ActionDeChariteRepository actionDeChariteRepository) {
        this.actionDeChariteRepository = actionDeChariteRepository;
    }
    
    @Override
    public List<ActionDeCharite> getAllActions() {
        return actionDeChariteRepository.findAll();
    }
    
    @Override
    public ActionDeCharite getActionById(Long id) {
        return actionDeChariteRepository.findById(id).orElse(null);
    }
    
    @Override
    public ActionDeCharite saveAction(ActionDeCharite action) {
        return actionDeChariteRepository.save(action);
    }
    
    @Override
    public void deleteAction(Long id) {
        actionDeChariteRepository.deleteById(id);
    }
    
    @Override
    public List<ActionDeCharite> getActionsByCategorie(Long categorieId) {
        return actionDeChariteRepository.findAll().stream()
            .filter(action -> action.getCategorie() != null && 
                    action.getCategorie().getId().equals(categorieId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ActionDeCharite> getActionsByOrganisation(Long organisationId) {
        return actionDeChariteRepository.findAll().stream()
            .filter(action -> action.getOrganisation() != null && 
                    action.getOrganisation().getId().equals(organisationId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ActionDeCharite> getActionsEnCours() {
        Date now = new Date();
        return actionDeChariteRepository.findAll().stream()
            .filter(action -> !action.isEstArchivee() && 
                    action.getDateDebut().before(now) && 
                    action.getDateFin().after(now))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ActionDeCharite> getActionsTerminees() {
        Date now = new Date();
        return actionDeChariteRepository.findAll().stream()
            .filter(action -> action.isEstArchivee() || 
                    action.getDateFin().before(now))
            .collect(Collectors.toList());
    }
} 