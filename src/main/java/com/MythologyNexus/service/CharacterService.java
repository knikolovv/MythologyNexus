package com.MythologyNexus.service;

import com.MythologyNexus.dto.AssociatedCharacterDTO;
import com.MythologyNexus.dto.CharacterDTO;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.Mythology;
import com.MythologyNexus.repository.CharacterRepo;
import com.MythologyNexus.repository.MythologyRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CharacterService {
    private final CharacterRepo characterRepo;

    private final MythologyRepo mythologyRepo;

    @Autowired
    public CharacterService(CharacterRepo characterRepo, MythologyRepo mythologyRepo) {
        this.characterRepo = characterRepo;
        this.mythologyRepo = mythologyRepo;
    }

    public List<Character> getAllCharacters() {
        return characterRepo.findAll();
    }

    public Character findCharacterById(Long id) {
        return characterRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Character was not found!"));
    }

    public CharacterDTO findFullCharacterByName(String name) {
        Character character = characterRepo.findByName(name).orElse(null);
        if (character == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Character with name " + name + " was not found!");
        }
        return new CharacterDTO(character.getId(), character.getName(), character.getDescription(), character.getMythology().getName(), character.getPowers(), findAssociatedCharacters(character));
    }

    public CharacterDTO findCharacterDTOById(Long id) {
        Character character = characterRepo.findById(id).orElse(null);

        if (character == null) {
            return null;
        }
        return new CharacterDTO(character.getId(), character.getName(), character.getDescription(), character.getMythology().getName(), character.getPowers(), findAssociatedCharacters(character));
    }

    public Character createOrUpdateCharacter(Character character) {
        Mythology mythology = character.getMythology();

        Optional<Mythology> existingMythology = mythologyRepo.findByName(mythology.getName());

        if (existingMythology.isPresent()) {
            character.setMythology(existingMythology.get());
        } else {
            mythologyRepo.save(mythology);
        }

        return characterRepo.save(character);
    }

    public void deleteCharacter(Long id) {
        characterRepo.deleteById(id);
    }

    public ResponseEntity<Character> addAssociatedCharacter(Long primaryCharacterId, String associatedCharacterName) {
        Character primaryCharacter = characterRepo.findById(primaryCharacterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Primary character not found"));
        Character associatedCharacter = characterRepo.findByName(associatedCharacterName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Associated character not found"));

        if (!primaryCharacter.getAssociatedCharacters().contains(associatedCharacter)) {
            primaryCharacter.addAssociatedCharacters(associatedCharacter);
            associatedCharacter.addAssociatedCharacters(primaryCharacter);
            characterRepo.save(primaryCharacter);
            characterRepo.save(associatedCharacter);

        }
        return ResponseEntity.ok(primaryCharacter);
    }

    private Set<AssociatedCharacterDTO> findAssociatedCharacters(Character character) {
        return character.getAssociatedCharacters().stream()
                .map(associatedCharacter -> new AssociatedCharacterDTO(associatedCharacter.getName()))
                .collect(Collectors.toSet());
    }


}
