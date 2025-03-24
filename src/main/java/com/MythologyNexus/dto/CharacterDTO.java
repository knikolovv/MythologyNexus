package com.MythologyNexus.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CharacterDTO(Long id, @NotBlank(message = "Character name must not be empty!") String name,
                           String description, String type, MythologyDTO mythology,
                           List<PowerDTO> powers, List<String> associatedCharacterNames, List<ArtefactDTO> artefacts) {
}
