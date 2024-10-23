package com.MythologyNexus.service;

import com.MythologyNexus.dto.AssociatedCharacterDTO;
import com.MythologyNexus.dto.CharacterDTO;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.Mythology;
import com.MythologyNexus.model.Power;
import com.MythologyNexus.repository.CharacterRepo;
import com.MythologyNexus.repository.MythologyRepo;
import com.MythologyNexus.repository.PowerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CharacterService {
    private final CharacterRepo characterRepo;

    private final MythologyRepo mythologyRepo;

    private final PowerRepo powerRepo;

    @Autowired
    public CharacterService(CharacterRepo characterRepo, MythologyRepo mythologyRepo, PowerRepo powerRepo) {
        this.characterRepo = characterRepo;
        this.mythologyRepo = mythologyRepo;
        this.powerRepo = powerRepo;
    }

    @Autowired


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

        Set<String> powerNames = character.getPowers().stream()
                .map(Power::getPowerName)
                .collect(Collectors.toSet());


        return new CharacterDTO(character.getName(),
                character.getDescription(),
                character.getType(),
                character.getMythology().getName(),
                powerNames,
                findAssociatedCharacters(character));
    }

    public CharacterDTO findCharacterDTOById(Long id) {
        Character character = characterRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Character with " + id + " was not found"));

        Set<String> powerNames = character.getPowers().stream()
                .map(Power::getPowerName)
                .collect(Collectors.toSet());

        return new CharacterDTO(character.getName(),
                character.getDescription(),
                character.getType(),
                character.getMythology().getName(),
                powerNames,
                findAssociatedCharacters(character));
    }

    public Character createCharacter(Character character) {
        Mythology mythology = character.getMythology();

        Optional<Mythology> existingMythology = mythologyRepo.findByName(mythology.getName());

        if (existingMythology.isPresent()) {
            character.setMythology(existingMythology.get());
        } else {
            mythologyRepo.save(mythology);
        }

        Set<Power> powers = new HashSet<>();
        for (Power power : character.getPowers()) {
            Power existingPower = powerRepo.findByPowerName(power.getPowerName())
                    .orElseGet(() -> powerRepo.save(power));
            powers.add(existingPower);
        }

        character.setPowers(powers);
        return characterRepo.save(character);
    }

    public Character updateCharacter(Long id,Character updatedCharacter) {

        Character existingCharacter = characterRepo.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Character not found!"));


        Mythology mythology = updatedCharacter.getMythology();

        if (mythology != null) {
            mythologyRepo.findByName(mythology.getName())
                    .ifPresentOrElse(existingCharacter::setMythology,
                    () -> {
                        Mythology newMythology= mythologyRepo.save(mythology);
                        existingCharacter.setMythology(newMythology);
                    });
        }


        if (updatedCharacter.getPowers() != null && !updatedCharacter.getPowers().isEmpty()) {
            Set<Power> updatedPowers = updatedCharacter.getPowers().stream()
                    .map(power -> powerRepo.findByPowerName(power.getPowerName())
                            .orElseGet(() -> powerRepo.save(power)))
                    .collect(Collectors.toSet());
            existingCharacter.setPowers(updatedPowers);
        }


        Optional.ofNullable(updatedCharacter.getName())
                .ifPresent(existingCharacter::setName);

        Optional.ofNullable(updatedCharacter.getDescription())
                .ifPresent(existingCharacter::setDescription);

        Optional.ofNullable(updatedCharacter.getType())
                .ifPresent(existingCharacter::setType);

        return characterRepo.save(existingCharacter);
    }

    public void deleteCharacterById(Long id) {
        characterRepo.deleteById(id);
    }

    public ResponseEntity<Character> addAssociatedCharacter(String primaryCharacterName, String associateCharacterName) {
        Character primaryCharacter = characterRepo.findByName(primaryCharacterName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Primary character not found"));
        Character associatedCharacter = characterRepo.findByName(associateCharacterName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Associated character not found"));

        if (!primaryCharacter.getAssociatedCharacters().contains(associatedCharacter)) {
            primaryCharacter.addAssociatedCharacter(associatedCharacter);
            associatedCharacter.addAssociatedCharacter(primaryCharacter);
            characterRepo.save(primaryCharacter);
            characterRepo.save(associatedCharacter);

        }
        return ResponseEntity.ok(primaryCharacter);
    }

    public ResponseEntity<Character> removeAssociatedCharacter(String primaryCharacterName, String associateCharacterName) {
        Character primaryCharacter = characterRepo.findByName(primaryCharacterName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Primary character not found"));
        Character associatedCharacter = characterRepo.findByName(associateCharacterName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Associated character not found"));

        if (primaryCharacter.getAssociatedCharacters().contains(associatedCharacter)) {
            primaryCharacter.removeAssociatedCharacter(associatedCharacter);
            associatedCharacter.removeAssociatedCharacter(primaryCharacter);
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
