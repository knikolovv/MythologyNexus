package com.MythologyNexus.repository;

import com.MythologyNexus.model.Artefact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtefactRepo extends JpaRepository<Artefact,Long> {
    Optional<Artefact> findByNameIgnoreCase(String name);

    @Query("SELECT COUNT(c) FROM Character c JOIN c.artefacts a WHERE a.id = :artefactId")
    Long countCharacterByArtefactId(@Param("artefactId") Long artefactId);
}
