package com.MythologyNexus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
@Table(name = "powers")
public class Power {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long powerId;

    @NotBlank
    private String powerName;

    public Power() {
    }

    public Power(String powerName) {
        this.powerName = powerName;
    }

    public void setPowerId(Long powerId) {
        this.powerId = powerId;
    }

    public Long getPowerId() {
        return powerId;
    }

    public String getPowerName() {
        return powerName;
    }

    public void setPowerName(String powerName) {
        this.powerName = powerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Power power)) return false;
        return powerName != null &&
               powerName.equalsIgnoreCase(power.getPowerName());
    }

    @Override
    public int hashCode() {
        return powerName != null ? powerName.toLowerCase().hashCode() : 0;
    }
}
