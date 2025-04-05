package com.MythologyNexus.controller;

import com.MythologyNexus.dto.PowerDTO;
import com.MythologyNexus.model.Power;
import com.MythologyNexus.service.PowerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/powers")
public class PowerController {
    private final PowerService powerService;

    @Autowired
    public PowerController(PowerService powerService) {
        this.powerService = powerService;
    }

    @GetMapping()
    public ResponseEntity<List<PowerDTO>> findAllPowers() {
        List<PowerDTO> allPowers = powerService.getAllPowers();
        return ResponseEntity.ok(allPowers);
    }

    @PostMapping()
    public ResponseEntity<Power> createPower(@Valid @RequestBody Power power) {
        Power savedPower = powerService.createPower(power);
        return ResponseEntity.ok(savedPower);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Power> getPowerById(@PathVariable Long id) {
        Power power = powerService.findPowerById(id);
        return ResponseEntity.ok(power);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PowerDTO> updatePower(@PathVariable Long id, @Valid @RequestBody Power power) {
        PowerDTO updatePower = powerService.updatePowerById(id, power);
        return ResponseEntity.ok(updatePower);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePower(@PathVariable Long id) {
        powerService.deletePower(id);
        return ResponseEntity.ok().build();
    }
}
