package com.MythologyNexus.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Character")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Character name must not be empty!")
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private CharacterType type;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "mythology_id", nullable = false)
    private Mythology mythology;

    @ManyToMany
    @JoinTable(name = "character_associated_powers",
            joinColumns = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "power_id"))
    private List<Power> powers;

    @ManyToMany
    @JoinTable(name = "character_associated_characters",
            joinColumns = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "associated_characters_id"))
    @JsonIgnoreProperties("associatedCharacters")
    private List<Character> associatedCharacters;


    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "character_associated_artefacts",
            joinColumns = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "associated_artefact_id"))
    private List<Artefact> artefacts = new ArrayList<>();

    public Character() {
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

    public CharacterType getType() {
        return type;
    }

    public void setType(CharacterType type) {
        this.type = type;
    }

    public List<Power> getPowers() {
        return powers;
    }

    public void setPowers(List<Power> powers) {
        this.powers = powers;
    }

    public Mythology getMythology() {
        return mythology;
    }

    public void setMythology(Mythology mythology) {
        this.mythology = mythology;
    }

    public List<Character> getAssociatedCharacters() {
        return associatedCharacters;
    }

    public void addAssociatedCharacter(Character character) {
        if (character != null) {
            associatedCharacters.add(character);
        }
    }

    public void removeAssociatedCharacter(Character character) {
        if (character != null) {
            associatedCharacters.remove(character);
        }
    }

    public List<Artefact> getArtefacts() {
        return artefacts;
    }

    public void setArtefacts(List<Artefact> characterArtefacts) {
        this.artefacts = characterArtefacts;
    }
}
