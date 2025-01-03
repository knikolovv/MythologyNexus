package com.MythologyNexus.repository;

import com.MythologyNexus.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CharacterRepo extends JpaRepository<Character, Long> {
    Optional<Character> findByNameIgnoreCase(String name);

    List<Character> findByMythologyId(Long id);

    List<Character> findByTypeIgnoreCase(String type);
}