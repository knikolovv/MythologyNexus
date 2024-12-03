package com.MythologyNexus.dto;

import java.util.List;

public class CharacterDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private String mythology;
    private List<String> powers;
    private List<String> associatedCharacters;
    private List<String> associatedArtefacts;


    public CharacterDTO(Long id, String name, String description, String type, String mythology, List<String> powers, List<String> associatedArtefacts, List<String> associatedCharacters) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.mythology = mythology;
        this.powers = powers;
        this.associatedCharacters = associatedCharacters;
        this.associatedArtefacts = associatedArtefacts;
    }

    public CharacterDTO() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMythology() {
        return mythology;
    }

    public void setMythology(String mythology) {
        this.mythology = mythology;
    }

    public List<String> getPowers() {
        return powers;
    }

    public void setPowers(List<String> powers) {
        this.powers = powers;
    }

    public List<String> getAssociatedCharacters() {
        return associatedCharacters;
    }

    public void setAssociatedCharacters(List<String> associatedCharacters) {
        this.associatedCharacters = associatedCharacters;
    }

    public List<String> getAssociatedArtefacts() {
        return associatedArtefacts;
    }

    public void setAssociatedArtefacts(List<String> associatedArtefacts) {
        this.associatedArtefacts = associatedArtefacts;
    }
}
