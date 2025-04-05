package com.MythologyNexus.repository;

import com.MythologyNexus.model.Artefact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtefactRepo extends JpaRepository<Artefact, Long> {
    Optional<Artefact> findByNameIgnoreCase(String name);
}
