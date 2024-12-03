package com.MythologyNexus.repository;

import com.MythologyNexus.model.Artefact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtefactRepo extends JpaRepository<Artefact,Long> {
    Optional<Artefact> findByName(String name);
}
