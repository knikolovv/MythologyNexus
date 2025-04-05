package com.MythologyNexus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "powers")
public class Power {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Power name must not be empty!")
    private String name;

    public Power() {
    }

    public void setId(Long powerId) {
        this.id = powerId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String powerName) {
        this.name = powerName;
    }
}
