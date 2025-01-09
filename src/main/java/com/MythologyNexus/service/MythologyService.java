package com.MythologyNexus.service;

import com.MythologyNexus.dto.MythologyDTO;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.Mythology;
import com.MythologyNexus.repository.CharacterRepo;
import com.MythologyNexus.repository.MythologyRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MythologyService {
    private final MythologyRepo mythologyRepo;

    private final CharacterRepo characterRepo;

    @Autowired
    public MythologyService(MythologyRepo mythologyRepo, CharacterRepo characterRepo) {
        this.mythologyRepo = mythologyRepo;
        this.characterRepo = characterRepo;
    }

    public List<MythologyDTO> findAllMythologies() {
        return mythologyRepo.findAll()
                .stream()
                .map(this::mythologyToMythologyDTO)
                .toList();
    }

    public Mythology createMythology(Mythology mythology) {
        return mythologyRepo.save(mythology);
    }

    public MythologyDTO updateMythology(Long id, Mythology updatedMythology) {
        Mythology existingMythology = mythologyRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Mythology was not found"));

        Optional.ofNullable(updatedMythology.getName())
                .ifPresent(existingMythology::setName);

        Optional.ofNullable(updatedMythology.getDescription())
                .ifPresent(existingMythology::setDescription);

        mythologyRepo.save(existingMythology);
        return mythologyToMythologyDTO(existingMythology);
    }

    public void deleteMythology(Long id) {
        List<Character> associatedCharacters = characterRepo.findByMythologyId(id);

        if (!associatedCharacters.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete mythology with ID " + id + " because it's associated with one or more characters.");
        }
        mythologyRepo.deleteById(id);
    }

    public MythologyDTO findMythologyById(Long id) {
        Mythology mythology = mythologyRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Mythology was not found!"));

        return mythologyToMythologyDTO(mythology);
    }

    private MythologyDTO mythologyToMythologyDTO(Mythology mythology) {
        return new MythologyDTO(mythology);
    }
}
