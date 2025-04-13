package com.charityapp.services;

import com.charityapp.entities.ActionDeCharite;
import com.charityapp.repositories.ActionDeChariteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ActionDeChariteService {
    
    private final ActionDeChariteRepository actionDeChariteRepository;
    
    public ActionDeChariteService(ActionDeChariteRepository actionDeChariteRepository) {
        this.actionDeChariteRepository = actionDeChariteRepository;
    }
    
    public List<ActionDeCharite> getAllActionsDeCharite() {
        return actionDeChariteRepository.findAll();
    }
    
    public ActionDeCharite getActionDeChariteById(Long id) {
        return actionDeChariteRepository.findById(id).orElse(null);
    }
    
    public ActionDeCharite saveActionDeCharite(ActionDeCharite actionDeCharite) {
        return actionDeChariteRepository.save(actionDeCharite);
    }
    
    public void deleteActionDeCharite(Long id) {
        actionDeChariteRepository.deleteById(id);
    }
} 