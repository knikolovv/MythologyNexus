package com.MythologyNexus.repository;

import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.CharacterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepo extends JpaRepository<Character, Long> {
    Optional<Character> findByNameIgnoreCase(String name);

    List<Character> findByMythologyId(Long id);

    List<Character> findByType(CharacterType type);

    List<Character> findByArtefactsId(Long id);
}
