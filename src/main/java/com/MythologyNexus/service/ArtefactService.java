package com.MythologyNexus.service;

import com.MythologyNexus.dto.ArtefactDTO;
import com.MythologyNexus.dto.ArtefactMapper;
import com.MythologyNexus.model.Artefact;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.repository.ArtefactRepo;
import com.MythologyNexus.repository.CharacterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ArtefactService {
    private final ArtefactRepo artefactRepo;
    private final CharacterRepo characterRepo;
    private final ArtefactMapper artefactMapper;

    @Autowired
    public ArtefactService(ArtefactRepo artefactRepo, CharacterRepo characterRepo, ArtefactMapper artefactMapper) {
        this.artefactRepo = artefactRepo;
        this.characterRepo = characterRepo;
        this.artefactMapper = artefactMapper;
    }

    public List<ArtefactDTO> getAllArtefacts() {
        return artefactRepo.findAll()
                .stream()
                .map(artefactMapper::toDto)
                .toList();
    }

    public Artefact createArtefact(Artefact artefact) {
        return artefactRepo.save(artefact);
    }

    public ArtefactDTO findArtefactByName(String name) {
        Artefact artefact = artefactRepo.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artefact with " + name + "was not found!"));
        return artefactMapper.toDto(artefact);
    }

    public void deleteArtefactById(Long id) {
        List<Character> associatedCharacters = characterRepo.findByArtefactsId(id);

        if (!associatedCharacters.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete artefact with ID " + id + " because it's associated with one or more characters.");
        }
        artefactRepo.deleteById(id);
    }

    public ArtefactDTO updateArtefact(Long id, Artefact artefact) {
        Artefact existingArtefact = artefactRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artefact with " + id + " was not found!"));

        Optional.ofNullable(artefact.getName())
                .ifPresent(existingArtefact::setName);

        Optional.ofNullable(artefact.getDescription())
                .ifPresent(existingArtefact::setDescription);
        artefactRepo.save(existingArtefact);
        return artefactMapper.toDto(existingArtefact);
    }
}
