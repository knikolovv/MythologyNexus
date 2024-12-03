package com.MythologyNexus.service;

import com.MythologyNexus.model.Artefact;
import com.MythologyNexus.repository.ArtefactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ArtefactService {

    private final ArtefactRepo artefactRepo;


    @Autowired
    public ArtefactService(ArtefactRepo artefactRepo) {
        this.artefactRepo = artefactRepo;
    }

    public List<Artefact> getAllArtefacts() {
        return artefactRepo.findAll();
    }

    public Artefact createArtefact(Artefact artefact) {
        return artefactRepo.save(artefact);
    }

    public Artefact findArtefactByName(String name) {
        return artefactRepo.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Artefact with " + name + "was not found!"));
    }

    public void deleteArtefactById(Long id) {
        artefactRepo.deleteById(id);
    }


    public Artefact updateArtefact(Long id, Artefact artefact) {
        Artefact existingArtefact = artefactRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artefact with " + id + " was not found!"));

        Optional.ofNullable(artefact.getName())
                .ifPresent(existingArtefact::setName);

        Optional.ofNullable(artefact.getDescription())
                .ifPresent(existingArtefact::setDescription);

        return artefactRepo.save(existingArtefact);

    }
}
