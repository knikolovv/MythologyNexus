package com.MythologyNexus.controller;

import com.MythologyNexus.dto.CharacterDTO;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService characterService;
    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping("/create")
    public ResponseEntity<Character> createCharacter(@RequestBody Character character) {
        Character savedCharacter = characterService.createCharacter(character);
        return new ResponseEntity<>(savedCharacter, HttpStatus.CREATED);
    }

    @PatchMapping ("/update/{id}")
    public ResponseEntity<Character> updateCharacter(@PathVariable Long id, @RequestBody Character character) {

        if (characterService.findCharacterById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Character with " + id + " was not found!");
        }

        Character updatedCharacter = characterService.updateCharacter(id,character);
        return ResponseEntity.ok(updatedCharacter);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacterById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CharacterDTO> getCharacterById(@PathVariable Long id) {
        CharacterDTO characterDTO = characterService.findCharacterDTOById(id);
        if (characterDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(characterDTO);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<CharacterDTO> findCharacterByName(@PathVariable String name) {
        CharacterDTO characterDTO = characterService.findFullCharacterByName(name);
        return ResponseEntity.ok(characterDTO);
    }

    @PostMapping("/{characterId}/associate-by-name/{associatedCharacterName}")
    public ResponseEntity<Character> addAssociatedCharacter(@PathVariable Long characterId,@PathVariable String associatedCharacterName) {
        return characterService.addAssociatedCharacter(characterId,associatedCharacterName);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Character>> getAllCharacters() {
        List<Character> allCharacters = characterService.getAllCharacters();
        return ResponseEntity.ok(allCharacters);
    }
}
