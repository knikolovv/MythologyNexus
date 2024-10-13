package com.MythologyNexus.controller;

import com.MythologyNexus.model.Mythology;
import com.MythologyNexus.service.MythologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mythologies")
public class MythologyController {

    private final MythologyService mythologyService;

    @Autowired
    public MythologyController(MythologyService mythologyService) {
        this.mythologyService = mythologyService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Mythology> getMythologyById(@PathVariable Long id) {
        Mythology mythology = mythologyService.findMythologyById(id);

        if (mythology == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(mythology);

    }

    @PostMapping("/create")
    public Mythology createMythology(@RequestBody Mythology mythology) {
        return mythologyService.createMythology(mythology);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMythology(@PathVariable Long id) {
        mythologyService.deleteMythology(id);
        return ResponseEntity.noContent().build();
    }
}
