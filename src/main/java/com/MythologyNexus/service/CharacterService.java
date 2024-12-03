package com.MythologyNexus.service;

import com.MythologyNexus.dto.CharacterDTO;
import com.MythologyNexus.model.Artefact;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.Mythology;
import com.MythologyNexus.model.Power;
import com.MythologyNexus.repository.ArtefactRepo;
import com.MythologyNexus.repository.CharacterRepo;
import com.MythologyNexus.repository.MythologyRepo;
import com.MythologyNexus.repository.PowerRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CharacterService {
    private final CharacterRepo characterRepo;

    private final MythologyRepo mythologyRepo;

    private final PowerRepo powerRepo;
    private final ArtefactRepo artefactRepo;

    @Autowired
    public CharacterService(CharacterRepo characterRepo, MythologyRepo mythologyRepo, PowerRepo powerRepo, ArtefactRepo artefactRepo) {
        this.characterRepo = characterRepo;
        this.mythologyRepo = mythologyRepo;
        this.powerRepo = powerRepo;
        this.artefactRepo = artefactRepo;
    }

    public List<Character> getAllCharacters() {
        return characterRepo.findAll();
    }
    public CharacterDTO findCharacterByName(String name) {
        Character character = characterRepo.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Character with name " + name + " was not found!"));

        List<String> powerNames = character.getPowers().stream()
                .map(Power::getName)
                .collect(Collectors.toList());

        List<String> artefacts = character.getArtefacts().stream()
                .map(Artefact::getName)
                .collect(Collectors.toList());


        return new CharacterDTO(character.getId(),
                character.getName(),
                character.getDescription(),
                character.getType(),
                character.getMythology().getName(),
                powerNames,
                artefacts,
                findAssociatedCharacters(character));
    }

    public List<String> getAllCharactersNames() {
        return characterRepo.findAll().stream().map(Character::getName).collect(Collectors.toList());
    }

    public Character createCharacter(Character character) {
        Mythology mythology = character.getMythology();

        Optional<Mythology> existingMythology = mythologyRepo.findByName(mythology.getName());

        if (existingMythology.isPresent()) {
            character.setMythology(existingMythology.get());
        } else {
            mythologyRepo.save(mythology);
        }

        List<Power> powers = new ArrayList<>();
        for (Power power : character.getPowers()) {
            Power existingPower = powerRepo.findByName(power.getName())
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

        if (mythology != null && !mythology.getName().isEmpty()) {
            mythologyRepo.findByName(mythology.getName())
                    .ifPresentOrElse(existingCharacter::setMythology,
                    () -> {
                        Mythology newMythology= mythologyRepo.save(mythology);
                        existingCharacter.setMythology(newMythology);
                    });
        }


        if (updatedCharacter.getPowers() != null && !updatedCharacter.getPowers().isEmpty()) {
            List<Power> updatedPowers = updatedCharacter.getPowers().stream()
                    .map(power -> powerRepo.findByName(power.getName())
                            .orElseGet(() -> powerRepo.save(power)))
                    .collect(Collectors.toList());
            existingCharacter.setPowers(updatedPowers);
        }

        if (updatedCharacter.getArtefacts() != null && !updatedCharacter.getArtefacts().isEmpty()) {
            List<Artefact> updatedArtefacts = updatedCharacter.getArtefacts().stream()
                    .map(artefact -> artefactRepo.findByName(artefact.getName())
                            .orElseGet(()-> artefactRepo.save(artefact)))
                    .collect(Collectors.toList());
            existingCharacter.setArtefacts(updatedArtefacts);
        }


        Optional.ofNullable(updatedCharacter.getName())
                .ifPresent(existingCharacter::setName);

        Optional.ofNullable(updatedCharacter.getDescription())
                .ifPresent(existingCharacter::setDescription);

        Optional.ofNullable(updatedCharacter.getType())
                .ifPresent(existingCharacter::setType);

        Optional.ofNullable(updatedCharacter.getArtefacts())
                .ifPresent(existingCharacter::setArtefacts);


        return characterRepo.save(existingCharacter);
    }

    public void deleteCharacterById(Long id) {
        if(characterRepo.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No character with id " + id + " was found!");
        }
        else {
        characterRepo.deleteById(id); }

    }

    public List<CharacterDTO> findAllCharactersByType(String type) {
        List<Character> allCharactersByType = characterRepo.findByType(type);

        return allCharactersByType.stream().map(character -> {
            CharacterDTO characterDTO = new CharacterDTO();

            characterDTO.setId(character.getId());

            characterDTO.setName(character.getName());

            characterDTO.setDescription(character.getDescription() != null ?
                    character.getDescription() : null);

            characterDTO.setType(character.getType());

            characterDTO.setMythology(character.getMythology() != null ?
                    character.getMythology().getName() : null);

            characterDTO.setPowers(character.getPowers() != null ?
                    character.getPowers().stream()
                            .map(Power::getName)
                            .collect(Collectors.toList()) :
                    new ArrayList<>());

            characterDTO.setAssociatedArtefacts(character.getArtefacts() != null ?
                    character.getArtefacts().stream()
                            .map(Artefact::getName)
                            .collect(Collectors.toList()) :
                    new ArrayList<>());

            characterDTO.setAssociatedCharacters(findAssociatedCharacters(character));

            return characterDTO;
        }).collect(Collectors.toList());
    }

    public void addAssociatedCharacter(String primaryCharacterName, String associateCharacterName) {
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
        ResponseEntity.ok(primaryCharacter);
    }

    public void removeAssociatedCharacter(String primaryCharacterName, String associateCharacterName) {
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
        ResponseEntity.ok(primaryCharacter);
    }

    private List<String> findAssociatedCharacters(Character character) {
        return character.getAssociatedCharacters()
                .stream()
                .map(Character::getName)
                .collect(Collectors.toList());
    }

}
