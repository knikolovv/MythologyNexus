package com.MythologyNexus.repository;

import com.MythologyNexus.model.Power;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PowerRepo extends JpaRepository<Power,Long> {
    Optional<Power> findByNameIgnoreCase(String name);

    @Query("SELECT COUNT(c) FROM Character c JOIN c.powers p WHERE p.id = :powerId")
    Long countCharactersByPowerId(@Param("powerId") Long powerId);
}
