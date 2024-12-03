package com.MythologyNexus.controller;

import com.MythologyNexus.model.Mythology;
import com.MythologyNexus.service.MythologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mythologies")
public class MythologyController {

    private final MythologyService mythologyService;

    @Autowired
    public MythologyController(MythologyService mythologyService) {
        this.mythologyService = mythologyService;
    }

    @GetMapping()
    public ResponseEntity<List<Mythology>> findAllMythologies() {
        return ResponseEntity.ok(mythologyService.findAllMythologies());
    }

    @PostMapping()
    public Mythology createMythology(@RequestBody Mythology mythology) {
        return mythologyService.createMythology(mythology);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mythology> getMythologyById(@PathVariable Long id) {
        Mythology mythology = mythologyService.findMythologyById(id);
        return ResponseEntity.ok(mythology);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Mythology> updateMythology(@PathVariable Long id, @RequestBody Mythology mythology) {
        Mythology updateMythology = mythologyService.updateMythology(id,mythology);
        return ResponseEntity.ok(updateMythology);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMythology(@PathVariable Long id) {
        mythologyService.deleteMythology(id);
        return ResponseEntity.noContent().build();
    }
}
