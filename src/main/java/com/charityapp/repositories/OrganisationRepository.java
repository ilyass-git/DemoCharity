package com.charityapp.repositories;

import com.charityapp.entities.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
    // TODO: Add custom query methods if needed
} 