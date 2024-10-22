package com.MythologyNexus.repository;

import com.MythologyNexus.model.Power;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PowerRepo extends JpaRepository<Power,Long> {
    Optional<Power> findByPowerName(String powerName);
}
