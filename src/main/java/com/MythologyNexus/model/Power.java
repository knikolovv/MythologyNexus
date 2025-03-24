package com.MythologyNexus.model;

import jakarta.persistence.*;

@Entity
@Table(name = "powers")
public class Power {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
