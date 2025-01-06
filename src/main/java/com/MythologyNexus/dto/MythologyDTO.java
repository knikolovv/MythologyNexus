package com.MythologyNexus.dto;

import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.Mythology;

import java.util.List;
import java.util.stream.Collectors;

public class MythologyDTO {

    private Long id;

    private String name;
    private String description;

    private List<String> characters;

    public MythologyDTO(Mythology mythology) {
        this.id = mythology.getId();
        this.name = mythology.getName();
        this.description = mythology.getDescription();
        this.characters = mythology.getCharacters().stream()
                .map(Character::getName)
                .collect(Collectors.toList());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
