package com.MythologyNexus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

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

}
