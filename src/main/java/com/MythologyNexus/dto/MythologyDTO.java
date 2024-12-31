package com.MythologyNexus.dto;

import java.util.ArrayList;
import java.util.List;

public class MythologyDTO {
    private Long id;
    private String name;
    private String description;
    private List<String> characters = new ArrayList<>();

    public MythologyDTO(Long id, String name, String description, List<String> characters) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.characters = characters;
    }

    public MythologyDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }
}
