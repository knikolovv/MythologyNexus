package com.MythologyNexus.controller;

import com.MythologyNexus.model.Power;
import com.MythologyNexus.service.PowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/powers")
public class PowerController {

    private final PowerService powerService;

    @Autowired
    public PowerController(PowerService powerService) {
        this.powerService = powerService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Power> getPowerById(@PathVariable Long id) {
        Power power = powerService.findPowerById(id);

        if (power == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Power with " + id + " was not found!");
        }
        return ResponseEntity.ok(power);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Power>> findAllPowers() {
        List<Power> allPowers = powerService.getAllPowers();
        return ResponseEntity.ok(allPowers);
    }
}
