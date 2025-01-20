package com.MythologyNexus.dto;

import java.util.List;

public record CharacterDTO(Long id, String name, String description, String type, MythologyDTO mythology,
                           List<PowerDTO> powers, List<String> associatedCharacterNames, List<ArtefactDTO> artefacts) {
}
