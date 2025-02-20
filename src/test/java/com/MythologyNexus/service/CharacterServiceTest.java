package com.MythologyNexus.service;

import com.MythologyNexus.dto.CharacterDTO;
import com.MythologyNexus.dto.CharacterMapper;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.repository.CharacterRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock
    private CharacterRepo characterRepo;

    @Mock
    private CharacterMapper characterMapper;

    @InjectMocks
    private CharacterService characterService;

    @Test
    void testGetAllCharacters() {
        Character characterOne = new Character();
        characterOne.setId(1L);
        characterOne.setName("Harry");
        characterOne.setDescription("You are a wizard!");

        Character characterTwo = new Character();
        characterTwo.setId(2L);
        characterTwo.setName("Voldemort");

        List<Character> characterList = List.of(characterOne, characterTwo);

        CharacterDTO characterOneDTO = new CharacterDTO(1L, "Harry", "You are a wizard!", null, null, null, null, null);
        CharacterDTO characterTwoDTO = new CharacterDTO(2L, "Voldemort", null, null, null, null, null, null);

        List<CharacterDTO> characterDTOList = List.of(characterOneDTO, characterTwoDTO);

        when(characterRepo.findAll()).thenReturn(characterList);
        when(characterMapper.toDto(characterOne)).thenReturn(characterOneDTO);
        when(characterMapper.toDto(characterTwo)).thenReturn(characterTwoDTO);

        List<CharacterDTO> actualCharacterDTO = characterService.getAllCharacters();

        verify(characterRepo).findAll();

        assertEquals(actualCharacterDTO, characterDTOList);
    }

    @Test
    void testGetAllCharacterNames() {
        Character characterOne = new Character();
        characterOne.setId(1L);
        characterOne.setName("Bruce");

        Character characterTwo = new Character();
        characterTwo.setId(2L);
        characterTwo.setName("Clark");

        List<String> expectedCharacterNames = List.of("Bruce","Clark");

        when(characterRepo.findAll()).thenReturn(List.of(characterOne,characterTwo));

        List<String> actualNamesList = characterService.getAllCharactersNames();

        verify(characterRepo).findAll();

        assertEquals(expectedCharacterNames,actualNamesList);
    }
}