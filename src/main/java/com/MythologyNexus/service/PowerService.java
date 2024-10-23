package com.MythologyNexus.service;

import com.MythologyNexus.model.Power;
import com.MythologyNexus.repository.PowerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service

public class PowerService {

    private final PowerRepo powerRepo;


    @Autowired
    public PowerService(PowerRepo powerRepo) {
        this.powerRepo = powerRepo;
    }

    public Power findPowerById(Long id) {
        return powerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Power with was not found"));
    }

    public List<Power> getAllPowers() {
        return powerRepo.findAll();
    }
}
