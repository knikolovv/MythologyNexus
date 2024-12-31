package com.MythologyNexus.controller;

import com.MythologyNexus.dto.ArtefactDTO;
import com.MythologyNexus.model.Artefact;
import com.MythologyNexus.service.ArtefactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artefacts")
public class ArtefactController {
    private final ArtefactService artefactService;

    @Autowired
    public ArtefactController(ArtefactService artefactService) {
        this.artefactService = artefactService;
    }

    @PostMapping()
    public ResponseEntity<Artefact> createArtefact(@RequestBody Artefact artefact) {
        Artefact savedArtefact = artefactService.createArtefact(artefact);
        return new ResponseEntity<>(savedArtefact, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ArtefactDTO>> findAllArtefacts() {
        List<ArtefactDTO> artefacts = artefactService.getAllArtefacts();
        return ResponseEntity.ok(artefacts);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ArtefactDTO> getArtefactByName(@PathVariable String name) {
        ArtefactDTO artefact = artefactService.findArtefactByName(name);
        return ResponseEntity.ok(artefact);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ArtefactDTO> updateArtefactById(@PathVariable Long id, @RequestBody Artefact artefact) {
        ArtefactDTO updateArtefact = artefactService.updateArtefact(id,artefact);
        return ResponseEntity.ok(updateArtefact);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteArtefactById(@PathVariable Long id) {
        artefactService.deleteArtefactById(id);
        return ResponseEntity.ok().build();
    }

}
