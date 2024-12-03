package com.MythologyNexus.controller;

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
    public ResponseEntity<List<Artefact>> findAllArtefacts() {
        List<Artefact> artefacts = artefactService.getAllArtefacts();
        return ResponseEntity.ok(artefacts);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Artefact> getArtefactByName(@PathVariable String name) {
        Artefact artefact = artefactService.findArtefactByName(name);
        return ResponseEntity.ok(artefact);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Artefact> updateArtefactById(@PathVariable Long id,@RequestBody Artefact artefact) {
        Artefact updateArtefact = artefactService.updateArtefact(id,artefact);
        return ResponseEntity.ok(updateArtefact);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteArtefactById(@PathVariable Long id) {
        artefactService.deleteArtefactById(id);
        return ResponseEntity.ok().build();
    }

}
