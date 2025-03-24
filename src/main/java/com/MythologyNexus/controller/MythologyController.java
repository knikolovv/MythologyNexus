package com.MythologyNexus.controller;

import com.MythologyNexus.dto.MythologyDTO;
import com.MythologyNexus.model.Mythology;
import com.MythologyNexus.service.MythologyService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<MythologyDTO>> findAllMythologies() {
        return ResponseEntity.ok(mythologyService.findAllMythologies());
    }

    @PostMapping()
    public ResponseEntity<Mythology> createMythology(@Valid @RequestBody Mythology mythology) {
        return ResponseEntity.ok(mythologyService.createMythology(mythology));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MythologyDTO> getMythologyById(@PathVariable Long id) {
        MythologyDTO mythology = mythologyService.findMythologyById(id);
        return ResponseEntity.ok(mythology);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MythologyDTO> updateMythology(@PathVariable Long id,@Valid @RequestBody Mythology mythology) {
        MythologyDTO updateMythology = mythologyService.updateMythology(id, mythology);
        return ResponseEntity.ok(updateMythology);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMythology(@PathVariable Long id) {
        mythologyService.deleteMythology(id);
        return ResponseEntity.noContent().build();
    }
}
