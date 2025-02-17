package com.MythologyNexus.service;

import com.MythologyNexus.dto.MythologyDTO;
import com.MythologyNexus.dto.MythologyMapper;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.Mythology;
import com.MythologyNexus.repository.CharacterRepo;
import com.MythologyNexus.repository.MythologyRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private static final Long validId = 1L;

    @Test
    public void testFindMythologyByIdThrowsErrorWhenGivenInvalidId() {
        Long invalidId = 50000L;
        when(mythologyRepo.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> mythologyService.findMythologyById(invalidId));
    }

    @Test
    public void testFindMythologyByIdWithValidIdReturnsMythologyDto() {
        Mythology mythology = new Mythology();
        mythology.setId(validId);
        mythology.setName("Greek");

        MythologyDTO expectedDto = new MythologyDTO(validId,"Greek",null,null);

        when(mythologyRepo.findById(validId)).thenReturn(Optional.of(mythology));
        when(mythologyMapper.toDto(mythology)).thenReturn(expectedDto);

        MythologyDTO actualDto = mythologyService.findMythologyById(validId);
        assertEquals(expectedDto,actualDto);
    }

    @Test
    public void testFindAllMythologiesReturnsListOfMythologyDto() {
        Mythology greekMythology = new Mythology();
        greekMythology.setId(1L);
        greekMythology.setName("Greek");

        Mythology norseMythology = new Mythology();
        norseMythology.setId(2L);
        norseMythology.setName("Norse");

        List<Mythology> mythologyList = List.of(greekMythology,norseMythology);

        MythologyDTO greekDto = new MythologyDTO(1L, "Greek",null,null);
        MythologyDTO norseDto = new MythologyDTO(2L,"Norse",null,null);

        List<MythologyDTO> expectedDtos = List.of(greekDto,norseDto);

        when(mythologyRepo.findAll()).thenReturn(mythologyList);
        when(mythologyMapper.toDto(greekMythology)).thenReturn(greekDto);
        when(mythologyMapper.toDto(norseMythology)).thenReturn(norseDto);

        List<MythologyDTO> actualDtos = mythologyService.findAllMythologies();

        assertEquals(expectedDtos,actualDtos);
    }

    @Test
    public void testCreateMythologyMethod() {
        Mythology mythology = new Mythology();
        mythology.setId(validId);
        mythology.setName("MythologyName");
        mythology.setDescription("Very descriptive! Wow!");

        when(mythologyRepo.save(mythology)).thenReturn(mythology);

        Mythology createdMythology = mythologyService.createMythology(mythology);

        verify(mythologyRepo).save(mythology);

        assertEquals(mythology,createdMythology);
    }

    @Test
    public void testDeleteMythologyMethodSuccess() {
        when(characterRepo.findByMythologyId(validId)).thenReturn(Collections.emptyList());

        mythologyService.deleteMythology(validId);

        verify(mythologyRepo).deleteById(validId);
    }

    @Test
    public void testDeleteMythologyMethodFailsWhenAssociatedCharactersExist() {

        when(characterRepo.findByMythologyId(validId)).thenReturn(List.of(new Character()));

        assertThrows(ResponseStatusException.class, () -> mythologyService.deleteMythology(validId));

        verify(mythologyRepo,never()).deleteById(anyLong());
    }


}