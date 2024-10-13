package com.MythologyNexus.dto;

import java.util.Set;

public class CharacterDTO {
    private Long id;
    private String name;
    private String description;
    private Set<String> powers;
    private String mythology;
    private Set<AssociatedCharacterDTO> associatedCharacters;


    public CharacterDTO(Long id, String name, String description, String mythologyName, Set<String> powers, Set<AssociatedCharacterDTO> associatedCharacters) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mythology = mythologyName;
        this.powers = powers;
        this.associatedCharacters = associatedCharacters;

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

    public String getMythology() {
        return mythology;
    }

    public void setMythology(String mythology) {
        this.mythology = mythology;
    }

    public Set<String> getPowers() {
        return powers;
    }

    public void setPowers(Set<String> powers) {
        this.powers = powers;
    }

    public Set<AssociatedCharacterDTO> getAssociatedCharacters() {
        return associatedCharacters;
    }

    public void setAssociatedCharacters(Set<AssociatedCharacterDTO> associatedCharacters) {
        this.associatedCharacters = associatedCharacters;
    }
}
