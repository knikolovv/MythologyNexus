package com.MythologyNexus.dto;

public class PowerDTO {

    private Long powerId;

    private String name;

    public PowerDTO(Long powerId, String name) {
        this.powerId = powerId;
        this.name = name;
    }

    public Long getId() {
        return powerId;
    }

    public void setPowerId(Long powerId) {
        this.powerId = powerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
