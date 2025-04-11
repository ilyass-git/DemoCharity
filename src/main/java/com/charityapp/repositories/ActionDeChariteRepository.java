package com.charityapp.repositories;

import com.charityapp.entities.ActionDeCharite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionDeChariteRepository extends JpaRepository<ActionDeCharite, Long> {
    // TODO: Add custom query methods if needed
} 