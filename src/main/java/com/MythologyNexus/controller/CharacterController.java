package com.MythologyNexus.controller;

import com.MythologyNexus.dto.CharacterDTO;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.CharacterType;
import com.MythologyNexus.service.CharacterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterController {
    private final CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping("/multipleFilters")
    public ResponseEntity<List<CharacterDTO>> getAllCharactersByTypeOrMythology(@RequestParam(required = false) String type,
                                                                                @RequestParam(required = false) String mythology) {
        List<CharacterDTO> characters = characterService.findCharacterByCriteriaUsingCriteriaAPI(type, mythology);
        return ResponseEntity.ok(characters);
    }

    @PostMapping()
    public ResponseEntity<CharacterDTO> createCharacter(@Valid @RequestBody Character character) {
        CharacterDTO savedCharacter = characterService.createCharacter(character);
        return new ResponseEntity<>(savedCharacter, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        List<CharacterDTO> allCharacters = characterService.getAllCharacters();
        return ResponseEntity.ok(allCharacters);
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllCharacterNames() {
        return ResponseEntity.ok(characterService.getAllCharactersNames());
    }

    @GetMapping("/{name}")
    public ResponseEntity<CharacterDTO> findCharacterByName(@PathVariable String name) {
        CharacterDTO characterDTO = characterService.findCharacterByName(name);
        return ResponseEntity.ok(characterDTO);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CharacterDTO>> findCharactersByType(@RequestParam CharacterType type) {
        List<CharacterDTO> characterDTOList = characterService.getAllCharactersByType(type);
        return ResponseEntity.ok(characterDTOList);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CharacterDTO> updateCharacter(@PathVariable Long id, @Valid @RequestBody Character character) {
        CharacterDTO updatedCharacter = characterService.updateCharacter(id, character);
        return ResponseEntity.ok(updatedCharacter);
    }

    @PatchMapping("/{characterName}/associate-with/{associateCharacterName}")
    public ResponseEntity<CharacterDTO> addAssociatedCharacter(@PathVariable String characterName
            , @PathVariable String associateCharacterName) {
        characterService.addAssociatedCharacter(characterName, associateCharacterName);
        return ResponseEntity.ok(characterService.findCharacterByName(characterName));
    }

    @PatchMapping("/{characterName}/remove-associate/{associateCharacterName}")
    public ResponseEntity<CharacterDTO> removeAssociatedCharacterByName(@PathVariable String characterName
            , @PathVariable String associateCharacterName) {
        characterService.removeAssociatedCharacter(characterName, associateCharacterName);
        return ResponseEntity.ok(characterService.findCharacterByName(characterName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacterById(id);
        return ResponseEntity.noContent().build();
    }
}
