package com.charityapp.services;

import com.charityapp.entities.Don;
import com.charityapp.repositories.DonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DonService {
    private final DonRepository donRepository;

    /**
     * Crée et sauvegarde un don dans la base de données
     * @param don Don à sauvegarder
     * @return Don sauvegardé
     */
    @Transactional
    public Don createDon(Don don) {
        return donRepository.save(don);
    }

    // TODO: Implement donation service methods
} 