package com.charityapp.repositories;

import com.charityapp.entities.Organisation;
import com.charityapp.entities.StatutOrganisation;
import com.charityapp.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
    Optional<Organisation> findByEmail(String email);
    List<Organisation> findByStatut(StatutOrganisation statut);
    List<Organisation> findByAdminId(Long adminId);
    Optional<Organisation> findByNom(String nom);
} 