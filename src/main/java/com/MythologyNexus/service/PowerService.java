package com.MythologyNexus.service;

import com.MythologyNexus.dto.PowerDTO;
import com.MythologyNexus.mappers.PowerMapper;
import com.MythologyNexus.model.Power;
import com.MythologyNexus.repository.PowerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PowerService {
    private final PowerRepo powerRepo;
    private final PowerMapper powerMapper;

    @Autowired
    public PowerService(PowerRepo powerRepo, PowerMapper powerMapper) {
        this.powerRepo = powerRepo;
        this.powerMapper = powerMapper;
    }

    public Power findPowerById(Long id) {
        return powerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Power with was not found"));
    }

    public List<PowerDTO> getAllPowers() {
        return powerRepo.findAll()
                .stream()
                .map(powerMapper::toDto)
                .toList();
    }

    public Power createPower(Power power) {
        return powerRepo.save(power);
    }

    public PowerDTO updatePowerById(Long id, Power power) {
        Power existingPower = powerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Power with id " + id + " was not found!"));

        Optional.ofNullable(power.getName())
                .ifPresent(existingPower::setName);
        powerRepo.save(existingPower);
        return powerMapper.toDto(existingPower);
    }

    public void deletePower(Long id) {
        powerRepo.deleteById(id);
    }
}
