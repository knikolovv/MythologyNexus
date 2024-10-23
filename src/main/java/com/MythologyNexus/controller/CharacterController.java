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

    @PostMapping("/create")
    public ResponseEntity<Character> createCharacter(@RequestBody Character character) {
        Character savedCharacter = characterService.createCharacter(character);
        return new ResponseEntity<>(savedCharacter, HttpStatus.CREATED);
    }

    @PatchMapping ("/update/{id}")
    public ResponseEntity<Character> updateCharacter(@PathVariable Long id, @RequestBody Character character) {
        Character updatedCharacter = characterService.updateCharacter(id,character);
        return ResponseEntity.ok(updatedCharacter);
    }

    @PatchMapping("update/{characterName}/remove-associate-by-name/{associateCharacterName}")
    public ResponseEntity<CharacterDTO> removeAssociatedCharacterByName(@PathVariable String characterName
            , @PathVariable String associateCharacterName) {
        characterService.removeAssociatedCharacter(characterName,associateCharacterName);
        return ResponseEntity.ok(characterService.findFullCharacterByName(characterName));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacterById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CharacterDTO> getCharacterById(@PathVariable Long id) {
        CharacterDTO characterDTO = characterService.findCharacterDTOById(id);
        return ResponseEntity.ok(characterDTO);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<CharacterDTO> findCharacterByName(@PathVariable String name) {
        CharacterDTO characterDTO = characterService.findFullCharacterByName(name);
        return ResponseEntity.ok(characterDTO);
    }

    @PatchMapping("/update/{characterName}/associate-by-name-with/{associateCharacterName}")
    public ResponseEntity<CharacterDTO> addAssociatedCharacter(@PathVariable String characterName
            , @PathVariable String associateCharacterName) {
        characterService.addAssociatedCharacter(characterName, associateCharacterName);
        return ResponseEntity.ok(characterService.findFullCharacterByName(characterName));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Character>> getAllCharacters() {
        List<Character> allCharacters = characterService.getAllCharacters();
        return ResponseEntity.ok(allCharacters);
    }
}
