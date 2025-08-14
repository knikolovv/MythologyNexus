package com.MythologyNexus.service;

import com.MythologyNexus.dto.CharacterDTO;
import com.MythologyNexus.mappers.CharacterMapper;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.*;
import com.MythologyNexus.repository.ArtefactRepo;
import com.MythologyNexus.repository.CharacterRepo;
import com.MythologyNexus.repository.MythologyRepo;
import com.MythologyNexus.repository.PowerRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CharacterService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final CharacterRepo characterRepo;
    private final MythologyRepo mythologyRepo;
    private final PowerRepo powerRepo;
    private final ArtefactRepo artefactRepo;
    private final CharacterMapper characterMapper;

    @Autowired
    public CharacterService(EntityManager entityManager, CharacterRepo characterRepo, MythologyRepo mythologyRepo, PowerRepo powerRepo,
                            ArtefactRepo artefactRepo, CharacterMapper characterMapper) {
        this.entityManager = entityManager;
        this.characterRepo = characterRepo;
        this.mythologyRepo = mythologyRepo;
        this.powerRepo = powerRepo;
        this.artefactRepo = artefactRepo;
        this.characterMapper = characterMapper;
    }

    public List<CharacterDTO> getAllCharacters() {
        return characterRepo.findAll()
                .stream()
                .map(characterMapper::toDto)
                .toList();
    }

    public CharacterDTO findCharacterByName(String name) {
        Character character = characterRepo.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Character with name " + name + " was not found!"));
        return characterMapper.toDto(character);
    }

    public List<String> getAllCharactersNames() {
        return characterRepo.findAll().stream()
                .map(Character::getName)
                .toList();
    }

    public CharacterDTO createCharacter(Character character) {
        Mythology mythology = character.getMythology();

        Optional<Mythology> existingMythology = mythologyRepo.findByNameIgnoreCase(mythology.getName());

        if (existingMythology.isPresent()) {
            character.setMythology(existingMythology.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Mythology " + character.getMythology().getName() + " doesn't exist!" +
                                                                    "Please create the Mythology first!");
        }

        List<Power> powers = new ArrayList<>();
        for (Power power : character.getPowers()) {
            Power existingPower = powerRepo.findByNameIgnoreCase(power.getName())
                    .orElseGet(() -> powerRepo.save(power));
            powers.add(existingPower);
        }

        character.setPowers(powers);
        characterRepo.save(character);
        return characterMapper.toDto(character);
    }

    public CharacterDTO updateCharacter(Long id, Character updatedCharacter) {
        Character existingCharacter = characterRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Character not found!"));

        Mythology mythology = updatedCharacter.getMythology();

        if (mythology != null && !mythology.getName().isEmpty()) {
            mythologyRepo.findByNameIgnoreCase(mythology.getName())
                    .ifPresentOrElse(existingCharacter::setMythology,
                            () -> existingCharacter.setMythology(mythology));
        }

        if (updatedCharacter.getPowers() != null && !updatedCharacter.getPowers().isEmpty()) {
            List<Power> updatedPowers = updatedCharacter.getPowers().stream()
                    .map(power -> powerRepo.findByNameIgnoreCase(power.getName())
                            .orElse(power))
                    .collect(Collectors.toList());

            existingCharacter.setPowers(updatedPowers);
        }

        if (updatedCharacter.getArtefacts() != null && !updatedCharacter.getArtefacts().isEmpty()) {

            List<Artefact> updatedArtefacts = updatedCharacter.getArtefacts().stream()
                    .map(artefact -> artefactRepo.findByNameIgnoreCase(artefact.getName())
                            .orElse(artefact))
                    .collect(Collectors.toList());

            existingCharacter.setArtefacts(updatedArtefacts);
        }

        Optional.ofNullable(updatedCharacter.getName())
                .ifPresent(existingCharacter::setName);

        Optional.ofNullable(updatedCharacter.getDescription())
                .ifPresent(existingCharacter::setDescription);

        Optional.ofNullable(updatedCharacter.getType())
                .ifPresent(existingCharacter::setType);

        characterRepo.save(existingCharacter);
        return characterMapper.toDto(existingCharacter);
    }

    public void deleteCharacterById(Long id) {
        if (characterRepo.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No character with id " + id + " was found!");
        } else {
            characterRepo.deleteById(id);
        }
    }

    public List<CharacterDTO> getAllCharactersByType(CharacterType type) {
        List<Character> allCharactersByType = characterRepo.findByType(type);

        return allCharactersByType.stream().map(characterMapper::toDto).toList();
    }

    public List<CharacterDTO> findCharacterByCriteriaUsingCriteriaAPI(String type, String mythologyName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Character> query = cb.createQuery(Character.class);
        Root<Character> root = query.from(Character.class);

        List<Predicate> predicates = new ArrayList<>();

        if (type != null && !type.isEmpty()) {
            predicates.add(cb.equal(root.get("type"), type));
        }
        if (mythologyName != null && !mythologyName.isEmpty()) {
            Optional<Mythology> mythology = mythologyRepo.findByNameIgnoreCase(mythologyName);
            mythology.ifPresent(mythologyFilter ->
                    predicates.add(cb.equal(root.get("mythology"), mythologyFilter))
            );
        }

        query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

        List<Character> characters = entityManager.createQuery(query).getResultList();

        return characters.stream()
                .map(characterMapper::toDto)
                .toList();
    }

    public void addAssociatedCharacter(String primaryCharacterName, String associateCharacterName) {
        Character primaryCharacter = characterRepo.findByNameIgnoreCase(primaryCharacterName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Primary character not found"));
        Character associatedCharacter = characterRepo.findByNameIgnoreCase(associateCharacterName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Associated character not found"));

        if (primaryCharacterName.equals(associateCharacterName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not associate a character with itself!");
        }

        if (!primaryCharacter.getAssociatedCharacters().contains(associatedCharacter)) {
            primaryCharacter.addAssociatedCharacter(associatedCharacter);
            associatedCharacter.addAssociatedCharacter(primaryCharacter);
            characterRepo.save(primaryCharacter);
        }
        ResponseEntity.ok(primaryCharacter);
    }

    public void removeAssociatedCharacter(String primaryCharacterName, String associateCharacterName) {
        Character primaryCharacter = characterRepo.findByNameIgnoreCase(primaryCharacterName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Primary character not found"));
        Character associatedCharacter = characterRepo.findByNameIgnoreCase(associateCharacterName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Associated character not found"));

        if (primaryCharacter.getAssociatedCharacters().contains(associatedCharacter)) {
            primaryCharacter.removeAssociatedCharacter(associatedCharacter);
            associatedCharacter.removeAssociatedCharacter(primaryCharacter);
            characterRepo.save(primaryCharacter);
        }
        ResponseEntity.ok(primaryCharacter);
    }
}
