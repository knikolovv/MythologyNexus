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
    private String name;

    public Power() {
    }

    public Power(String name) {
        this.name = name;
    }

    public void setPowerId(Long powerId) {
        this.powerId = powerId;
    }

    public Long getPowerId() {
        return powerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String powerName) {
        this.name = powerName;
    }

}
