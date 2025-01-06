package com.MythologyNexus.repository;

import com.MythologyNexus.model.Power;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PowerRepo extends JpaRepository<Power,Long> {
    Optional<Power> findByNameIgnoreCase(String name);
}
