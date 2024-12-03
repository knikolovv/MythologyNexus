package com.MythologyNexus.controller;

import com.MythologyNexus.dto.CharacterDTO;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.service.CharacterService;
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

    @PostMapping()
    public ResponseEntity<Character> createCharacter(@RequestBody Character character) {
        Character savedCharacter = characterService.createCharacter(character);
        return new ResponseEntity<>(savedCharacter, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Character>> getAllCharacters() {
        List<Character> allCharacters = characterService.getAllCharacters();
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
    public ResponseEntity<List<CharacterDTO>> findCharactersByType(
            @RequestParam String type) {
        List<CharacterDTO> characterDTOList = characterService.findAllCharactersByType(type);
        return ResponseEntity.ok(characterDTOList);
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<Character> updateCharacter(@PathVariable Long id, @RequestBody Character character) {
        Character updatedCharacter = characterService.updateCharacter(id,character);
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
        characterService.removeAssociatedCharacter(characterName,associateCharacterName);
        return ResponseEntity.ok(characterService.findCharacterByName(characterName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacterById(id);
        return ResponseEntity.noContent().build();
    }


}
