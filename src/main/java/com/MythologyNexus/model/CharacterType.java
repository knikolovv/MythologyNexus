package com.MythologyNexus.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CharacterType {
    GOD("God"), DEMIGOD("Demigod"), CREATURE("Creature"), HUMAN("Human");

    private final String characterType;

    CharacterType(String characterType) {
        this.characterType = characterType;
    }

    @JsonValue
    public String getCharacterType() {
        return characterType;
    }

    @JsonCreator
    public static CharacterType fromValue(String value) {
        for (CharacterType type : values()) {
            String currentContact = type.getCharacterType();
            if (currentContact.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid value for Character type Enum: " + value);
    }
}
