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

    @GetMapping("/findAll")
    public ResponseEntity<List<Mythology>> findAllMythologies() {
        return ResponseEntity.ok(mythologyService.findAllMythologies());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Mythology> getMythologyById(@PathVariable Long id) {
        Mythology mythology = mythologyService.findMythologyById(id);
        return ResponseEntity.ok(mythology);
    }

    @PostMapping("/create")
    public Mythology createMythology(@RequestBody Mythology mythology) {
        return mythologyService.createMythology(mythology);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Mythology> updateMythology(@PathVariable Long id, @RequestBody Mythology mythology) {
        Mythology updateMythology = mythologyService.updateMythology(id,mythology);
        return ResponseEntity.ok(updateMythology);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMythology(@PathVariable Long id) {
        mythologyService.deleteMythology(id);
        return ResponseEntity.noContent().build();
    }
}
