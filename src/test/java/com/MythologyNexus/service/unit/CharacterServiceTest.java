package com.MythologyNexus.service.unit;

import com.MythologyNexus.dto.*;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.*;
import com.MythologyNexus.repository.ArtefactRepo;
import com.MythologyNexus.repository.CharacterRepo;
import com.MythologyNexus.repository.MythologyRepo;
import com.MythologyNexus.repository.PowerRepo;
import com.MythologyNexus.service.CharacterService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
class CharacterServiceTest {
    @Mock
    private CharacterRepo characterRepo;
    @Mock
    private CharacterMapper characterMapper;
    @Mock
    private MythologyRepo mythologyRepo;
    @Mock
    private PowerRepo powerRepo;
    @Mock
    private ArtefactRepo artefactRepo;
    @Mock
    private EntityManager entityManager;
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

        List<String> expectedCharacterNames = List.of("Bruce", "Clark");

        when(characterRepo.findAll()).thenReturn(List.of(characterOne, characterTwo));

        List<String> actualNamesList = characterService.getAllCharactersNames();

        verify(characterRepo).findAll();

        assertEquals(expectedCharacterNames, actualNamesList);
    }

    @Test
    void testCreateCharacterMethodWithInValidData() {
        Mythology mythology = new Mythology();
        mythology.setId(1L);
        mythology.setName("Marvel");
        mythology.setDescription("Assemble");

        Power power = new Power();
        power.setId(1L);
        power.setName("Money");

        Character character = new Character();
        character.setId(1L);
        character.setName("Tony Stark");
        character.setDescription("Genius,Billionaire,Playboy,Philanthropist");
        character.setType(CharacterType.HUMAN);
        character.setMythology(mythology);
        character.setPowers(List.of(power));

        when(mythologyRepo.findByNameIgnoreCase(mythology.getName())).thenReturn(Optional.of(mythology));
        when(powerRepo.findByNameIgnoreCase(power.getName())).thenReturn(Optional.empty());
        when(powerRepo.save(power)).thenReturn(power);

        MythologyDTO mythologyDTO = new MythologyDTO(1L, "Marvel", "Assemble", null);
        PowerDTO powerDTO = new PowerDTO(1L, "Money");

        CharacterDTO characterDTO = new CharacterDTO(1L, "Tony stark", "Genius,Billionaire,Playboy,Philanthropist",
                CharacterType.HUMAN.getCharacterType(), mythologyDTO, List.of(powerDTO), null, null);

        when(characterMapper.toDto(character)).thenReturn(characterDTO);

        CharacterDTO savedCharacterDTO = characterService.createCharacter(character);

        assertEquals(characterDTO, savedCharacterDTO);

        verify(characterRepo).save(character);
        verify(characterMapper).toDto(character);
    }

    @Test
    void testCreateCharacterMethodThrowsResponseStatusExceptionWithNonExistingMythology() {
        Mythology mythology = new Mythology();
        mythology.setId(1L);
        mythology.setName("Marvel");
        mythology.setDescription("Assemble");

        Character character = new Character();
        character.setId(1L);
        character.setName("Tony Stark");
        character.setDescription("Genius,Billionaire,Playboy,Philanthropist");
        character.setType(CharacterType.HUMAN);
        character.setMythology(mythology);
        character.setPowers(Collections.emptyList());

        when(mythologyRepo.findByNameIgnoreCase(mythology.getName())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> characterService.createCharacter(character));

        String expectedMessage = "The Mythology " + mythology.getName() + " doesn't exist!" +
                                 "Please create the Mythology first!";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testUpdateCharacterMethodThrowsResponseStatusExceptionWhenGivenNonExistingCharacterId() {
        Character updatedCharacter = new Character();

        when(characterRepo.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> characterService.updateCharacter(1L, updatedCharacter));

        String expectedMessage = "Character not found!";

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testUpdateCharacterMethodWithValidData() {
        // Setup existing object data
        Mythology existingMythology = new Mythology();
        existingMythology.setId(1L);
        existingMythology.setName("Detective Comics");
        existingMythology.setDescription("Truth, Justice and a Better Tomorrow");

        Power superStrength = new Power();
        superStrength.setId(1L);
        superStrength.setName("Super Strength");

        Power flight = new Power();
        flight.setId(2L);
        flight.setName("Flight");

        List<Power> existingCharacterPowers = List.of(superStrength, flight);

        Artefact bracelets = new Artefact();
        bracelets.setId(1L);
        bracelets.setName("Bracelets of Submission");
        bracelets.setDescription("Bracelets made of fictional metal!");

        List<Artefact> existingCharacterArtefacts = List.of(bracelets);

        Character existingCharacter = new Character();
        existingCharacter.setId(1L);
        existingCharacter.setName("Wonder Woman");
        existingCharacter.setDescription("A superhero");
        existingCharacter.setType(CharacterType.DEMIGOD);
        existingCharacter.setMythology(existingMythology);
        existingCharacter.setPowers(existingCharacterPowers);
        existingCharacter.setArtefacts(existingCharacterArtefacts);

        // Updated Character and Attributes
        Mythology newMythology = new Mythology();
        newMythology.setId(2L);
        newMythology.setName("DC Comics");
        newMythology.setDescription("Truth, Justice, and the American Way");

        MythologyDTO newMythologyDTO = new MythologyDTO(2L, "DC Comics", "Truth, Justice, and the American Way", null);

        // Update Powers List
        Power superSpeed = new Power();
        superSpeed.setId(3L);
        superSpeed.setName("Super Speed");

        List<Power> updatedPowers = List.of(superStrength, superSpeed);
        PowerDTO superStrengthDTO = new PowerDTO(1L, "Super Strength");
        PowerDTO superSpeedDTO = new PowerDTO(3L, "Super Speed");

        // Update Artefact
        Artefact lasso = new Artefact();
        lasso.setId(2L);
        lasso.setName("Lasso of Truth");
        lasso.setDescription("A magical golden lariat");
        List<Artefact> updatedArtefacts = List.of(lasso);
        ArtefactDTO lassoDTO = new ArtefactDTO(2L, "Lasso of Truth", "A magical golden lariat");

        Character updatedCharacter = new Character();
        updatedCharacter.setName("Diana of Themyscira");
        updatedCharacter.setDescription("A citizen");
        updatedCharacter.setType(CharacterType.HUMAN);
        updatedCharacter.setMythology(newMythology);
        updatedCharacter.setPowers(updatedPowers);
        updatedCharacter.setArtefacts(updatedArtefacts);

        // Expected DTO after the update
        CharacterDTO updatedCharacterDTO = new CharacterDTO(1L, "Diana of Themyscira", "A citizen"
                , CharacterType.HUMAN.getCharacterType(), newMythologyDTO, List.of(superStrengthDTO, superSpeedDTO),
                null, List.of(lassoDTO));

        when(characterRepo.findById(1L)).thenReturn(Optional.of(existingCharacter));
        when(mythologyRepo.findByNameIgnoreCase(newMythology.getName())).thenReturn(Optional.empty());
        when(powerRepo.findByNameIgnoreCase("Super Strength")).thenReturn(Optional.of(superStrength));
        when(powerRepo.findByNameIgnoreCase("Super Speed")).thenReturn(Optional.of(superSpeed));
        when(artefactRepo.findByNameIgnoreCase("Lasso of Truth")).thenReturn(Optional.of(lasso));
        when(characterMapper.toDto(existingCharacter)).thenReturn(updatedCharacterDTO);

        CharacterDTO resultDTO = characterService.updateCharacter(1L, updatedCharacter);

        assertEquals(updatedCharacterDTO, resultDTO);

        verify(characterRepo).save(existingCharacter);
        verify(characterMapper).toDto(existingCharacter);

        assertEquals("Diana of Themyscira", existingCharacter.getName());
        assertEquals(CharacterType.HUMAN, existingCharacter.getType());
    }

    @Test
    public void testDeleteCharacterMethodThrowsErrorWhenGivenNonExistingId() {
        when(characterRepo.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException response = assertThrows(ResponseStatusException.class,
                () -> characterService.deleteCharacterById(1L));

        String expectedExceptionMessage = "No character with id " + 1L + " was found!";

        assertEquals(expectedExceptionMessage, response.getReason());

        verify(characterRepo, never()).deleteById(anyLong());
    }

    @Test
    public void testDeleteCharacterMethodDeletesCharacterSuccessfully() {
        Character character = new Character();
        character.setId(1L);
        character.setName("Ben Richards");
        character.setDescription("The Running Man");

        when(characterRepo.findById(1L)).thenReturn(Optional.of(character));

        characterService.deleteCharacterById(1L);

        verify(characterRepo).deleteById(1L);
    }

    @Test
    public void testGetAllCharacterByTypeMethod() {
        Character characterOne = new Character();
        characterOne.setId(1L);
        characterOne.setName("Horus");
        characterOne.setDescription("The Falcon God");
        characterOne.setType(CharacterType.GOD);

        Character characterTwo = new Character();
        characterTwo.setId(2L);
        characterTwo.setName("Seth");
        characterTwo.setDescription("The Canine God");
        characterTwo.setType(CharacterType.GOD);

        CharacterDTO characterOneDTO = new CharacterDTO(1L, "Horus", "The Falcon God", "GOD"
                , null, null, null, null);
        CharacterDTO characterTwoDTO = new CharacterDTO(2L, "Seth", "The Canine God", "GOD"
                , null, null, null, null);

        when(characterRepo.findByType(CharacterType.GOD)).thenReturn(List.of(characterOne, characterTwo));
        when(characterMapper.toDto(characterOne)).thenReturn(characterOneDTO);
        when(characterMapper.toDto(characterTwo)).thenReturn(characterTwoDTO);

        List<CharacterDTO> result = characterService.getAllCharactersByType(CharacterType.GOD);

        assertEquals(result, List.of(characterOneDTO, characterTwoDTO));

        verify(characterRepo).findByType(CharacterType.GOD);
        verify(characterMapper).toDto(characterOne);
        verify(characterMapper).toDto(characterTwo);
    }

    @Test
    public void testGetAllCharacterByTypeMethodReturnsEmptyListWhenNoCharactersFound() {
        when(characterRepo.findByType(CharacterType.HUMAN)).thenReturn(Collections.emptyList());

        List<CharacterDTO> result = characterService.getAllCharactersByType(CharacterType.HUMAN);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(characterRepo).findByType(CharacterType.HUMAN);
    }

    @Test
    public void testFindCharacterByCriteriaUsingCriteriaAPI_withTypeAndMythology() {
        String type = "Creature";
        String mythologyName = "DreamWorks";

        Mythology mythology = new Mythology();
        mythology.setId(1L);
        mythology.setName("DreamWorks");

        when(mythologyRepo.findByNameIgnoreCase(mythologyName)).thenReturn(Optional.of(mythology));

        Character character = new Character();
        character.setId(1L);
        character.setName("Shrek");
        character.setType(CharacterType.CREATURE);
        character.setMythology(mythology);

        CharacterDTO characterDTO = new CharacterDTO(1L, "Shrek", null, "Creature",
                new MythologyDTO(1L, "DreamWorks", null, null), null,
                null, null);

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Character> cq = mock(CriteriaQuery.class);
        Root<Character> root = mock(Root.class);
        TypedQuery<Character> typedQuery = mock(TypedQuery.class);
        Predicate predicate = mock(Predicate.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Character.class)).thenReturn(cq);
        when(cq.from(Character.class)).thenReturn(root);
        when(cq.select(root)).thenReturn(cq);

        lenient().when(cb.equal(any(), any())).thenReturn(predicate);
        lenient().when(cq.where((Predicate[]) any())).thenReturn(cq);

        when(entityManager.createQuery(cq)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(character));

        when(characterMapper.toDto(character)).thenReturn(characterDTO);

        List<CharacterDTO> result = characterService.findCharacterByCriteriaUsingCriteriaAPI(type,mythologyName);

        assertEquals(1,result.size());
        assertEquals(characterDTO,result.get(0));

        verify(entityManager).getCriteriaBuilder();
        verify(mythologyRepo).findByNameIgnoreCase(mythologyName);
    }

    @Test
    public void testAddAssociatedCharacterMethodSuccessfully() {
        String primaryCharacterName = "Littlefoot";
        String associateCharacterName = "Cera";

        Character primaryCharacter = new Character();
        primaryCharacter.setId(1L);
        primaryCharacter.setName("Littlefoot");

        Character associateCharacter = new Character();
        associateCharacter.setId(2L);
        associateCharacter.setName("Cera");

        when(characterRepo.findByNameIgnoreCase(primaryCharacterName)).thenReturn(Optional.of(primaryCharacter));
        when(characterRepo.findByNameIgnoreCase(associateCharacterName)).thenReturn(Optional.of(associateCharacter));

        characterService.addAssociatedCharacter(primaryCharacterName,associateCharacterName);

        assertTrue(primaryCharacter.getAssociatedCharacters().contains(associateCharacter));
        assertTrue(associateCharacter.getAssociatedCharacters().contains(primaryCharacter));

        verify(characterRepo).save(primaryCharacter);
    }

    @Test
    public void testRemoveAssociatedCharacterMethodSuccessfully() {
        String primaryCharacterName = "Littlefoot";
        String associateCharacterName = "Cera";

        Character primaryCharacter = new Character();
        primaryCharacter.setId(1L);
        primaryCharacter.setName("Littlefoot");

        Character associateCharacter = new Character();
        associateCharacter.setId(2L);
        associateCharacter.setName("Cera");

        primaryCharacter.addAssociatedCharacter(associateCharacter);
        associateCharacter.addAssociatedCharacter(primaryCharacter);

        assertTrue(primaryCharacter.getAssociatedCharacters().contains(associateCharacter));
        assertTrue(associateCharacter.getAssociatedCharacters().contains(primaryCharacter));

        when(characterRepo.findByNameIgnoreCase(primaryCharacterName)).thenReturn(Optional.of(primaryCharacter));
        when(characterRepo.findByNameIgnoreCase(associateCharacterName)).thenReturn(Optional.of(associateCharacter));

        characterService.removeAssociatedCharacter(primaryCharacterName,associateCharacterName);

        assertFalse(primaryCharacter.getAssociatedCharacters().contains(associateCharacter));
        assertFalse(associateCharacter.getAssociatedCharacters().contains(primaryCharacter));

        verify(characterRepo).save(primaryCharacter);
    }
}