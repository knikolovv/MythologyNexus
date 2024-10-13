package com.MythologyNexus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "mythology_id", nullable = false)
    @NotNull
    private Mythology mythology;
    @ElementCollection
    @CollectionTable(name = "character_powers")
    private Set<String> powers;

    @ManyToMany
    @JoinTable(name = "character_associated_characters",
            joinColumns = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "associated_characters_id"))
    private Set<Character> associatedCharacters = new HashSet<>();

    public Character() {
    }

    public Character(Long id, String name, String description, Set<String> powers, Mythology mythology, Set<Character> associatedCharacters) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.powers = powers;
        this.mythology = mythology;
        this.associatedCharacters = associatedCharacters;
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

    public Set<String> getPowers() {
        return powers;
    }

    public void setPowers(Set<String> powers) {
        this.powers = powers;
    }

    public Mythology getMythology() {
        return mythology;
    }

    public void setMythology(Mythology mythology) {
        this.mythology = mythology;
    }

    public Set<Character> getAssociatedCharacters() {
        return associatedCharacters;
    }

    public void setAssociatedCharacters(Set<Character> associatedCharacters) {
        this.associatedCharacters = associatedCharacters;
    }

    public void addAssociatedCharacters(Character character) {
        if (character != null) {
            associatedCharacters.add(character);
        }
    }

}
