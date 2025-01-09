package com.MythologyNexus.dto;

import com.MythologyNexus.model.Artefact;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.Power;

import java.util.List;

public class CharacterDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private String mythology;
    private List<String> powers;
    private List<String> associatedArtefacts;
    private List<String> associatedCharacters;


    public CharacterDTO(Character character) {
        this.id = character.getId();
        this.name = character.getName();
        this.description = character.getDescription();
        this.type = character.getType();
        this.mythology = character.getMythology().getName();
        this.powers = character.getPowers().stream()
                .map(Power::getName)
                .toList();
        this.associatedArtefacts = character.getArtefacts().stream()
                .map(Artefact::getName)
                .toList();
        this.associatedCharacters = character.getAssociatedCharacters()
                .stream()
                .map(Character::getName)
                .toList();
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
