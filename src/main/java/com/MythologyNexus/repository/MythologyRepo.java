package com.MythologyNexus.repository;

import com.MythologyNexus.model.Mythology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MythologyRepo extends JpaRepository<Mythology,Long> {
    Optional<Mythology> findByNameIgnoreCase(String name);
}
