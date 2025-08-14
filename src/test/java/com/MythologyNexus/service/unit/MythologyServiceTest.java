package com.MythologyNexus.service.unit;

import com.MythologyNexus.dto.MythologyDTO;
import com.MythologyNexus.mappers.MythologyMapper;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.Mythology;
import com.MythologyNexus.repository.CharacterRepo;
import com.MythologyNexus.repository.MythologyRepo;
import com.MythologyNexus.service.MythologyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MythologyServiceTest {
    @Mock
    private MythologyRepo mythologyRepo;
    @Mock
    private MythologyMapper mythologyMapper;
    @Mock
    private CharacterRepo characterRepo;
    @InjectMocks
    private MythologyService mythologyService;

    @Test
    public void testFindMythologyByIdThrowsErrorWhenGivenInvalidId() {
        when(mythologyRepo.findById(50000L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> mythologyService.findMythologyById(50000L));
    }

    @Test
    public void testFindMythologyByIdWithValidIdReturnsMythologyDto() {
        Mythology mythology = new Mythology();
        mythology.setId(1L);
        mythology.setName("Greek");

        MythologyDTO expectedDto = new MythologyDTO(1L, "Greek", null, null);

        when(mythologyRepo.findById(1L)).thenReturn(Optional.of(mythology));
        when(mythologyMapper.toDto(mythology)).thenReturn(expectedDto);

        MythologyDTO actualDto = mythologyService.findMythologyById(1L);
        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void testFindAllMythologiesReturnsListOfMythologyDto() {
        Mythology greekMythology = new Mythology();
        greekMythology.setId(1L);
        greekMythology.setName("Greek");

        Mythology norseMythology = new Mythology();
        norseMythology.setId(2L);
        norseMythology.setName("Norse");

        List<Mythology> mythologyList = List.of(greekMythology, norseMythology);

        MythologyDTO greekDto = new MythologyDTO(1L, "Greek", null, null);
        MythologyDTO norseDto = new MythologyDTO(2L, "Norse", null, null);

        List<MythologyDTO> expectedDtos = List.of(greekDto, norseDto);

        when(mythologyRepo.findAll()).thenReturn(mythologyList);
        when(mythologyMapper.toDto(greekMythology)).thenReturn(greekDto);
        when(mythologyMapper.toDto(norseMythology)).thenReturn(norseDto);

        List<MythologyDTO> actualDtos = mythologyService.findAllMythologies();

        assertEquals(expectedDtos, actualDtos);
    }

    @Test
    public void testCreateMythologyMethod() {
        Mythology mythology = new Mythology();
        mythology.setId(1L);
        mythology.setName("MythologyName");
        mythology.setDescription("Very descriptive! Wow!");

        when(mythologyRepo.save(mythology)).thenReturn(mythology);

        Mythology createdMythology = mythologyService.createMythology(mythology);

        verify(mythologyRepo).save(mythology);

        assertEquals(mythology, createdMythology);
    }

    @Test
    public void testDeleteMythologyMethodSuccess() {
        when(characterRepo.findByMythologyId(1L)).thenReturn(Collections.emptyList());

        mythologyService.deleteMythology(1L);

        verify(mythologyRepo).deleteById(1L);
    }

    @Test
    public void testDeleteMythologyMethodFailsWhenAssociatedCharactersExist() {
        when(characterRepo.findByMythologyId(1L)).thenReturn(List.of(new Character()));

        assertThrows(ResponseStatusException.class, () -> mythologyService.deleteMythology(1L));

        verify(mythologyRepo, never()).deleteById(anyLong());
    }

    @Test
    public void testUpdateMythologyMethodFailsWhenGivenWrongId() {
        Mythology mythology = new Mythology();
        when(mythologyRepo.findById(50000L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> mythologyService.updateMythology(50000L, mythology));
    }

    @Test
    public void testUpdateMythologyMethodWithValidId() {
        Mythology existingMythology = new Mythology();
        existingMythology.setId(1L);
        existingMythology.setName("MythologyName");
        existingMythology.setDescription("That's a description!");

        when(mythologyRepo.findById(1L)).thenReturn(Optional.of(existingMythology));

        Mythology updatedMythology = new Mythology();
        updatedMythology.setId(1L);
        updatedMythology.setName("New Mythology Name");

        MythologyDTO expectedDto = new MythologyDTO(1L, "New Mythology Name");

        when(mythologyMapper.toDto(existingMythology)).thenReturn(expectedDto);

        MythologyDTO actualDto = mythologyService.updateMythology(1L, updatedMythology);

        assertEquals("New Mythology Name", actualDto.name());

        verify(mythologyRepo).findById(anyLong());
        verify(mythologyRepo).save(existingMythology);
    }
}