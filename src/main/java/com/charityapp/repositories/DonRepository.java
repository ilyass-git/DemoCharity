package com.charityapp.repositories;

import com.charityapp.entities.Don;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonRepository extends JpaRepository<Don, Long> {
    // TODO: Add custom query methods if needed
} 