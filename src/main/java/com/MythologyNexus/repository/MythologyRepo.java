package com.MythologyNexus.repository;

import com.MythologyNexus.model.Mythology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MythologyRepo extends JpaRepository<Mythology,Long> {
    Optional<Mythology> findByNameIgnoreCase(String name);
}
