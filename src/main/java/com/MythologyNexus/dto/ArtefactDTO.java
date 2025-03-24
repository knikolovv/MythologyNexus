package com.MythologyNexus.dto;

import jakarta.validation.constraints.NotBlank;

public record ArtefactDTO(Long id,
                          @NotBlank(message = "Artefact name must not be Empty!") String name,
                          String description) {
}
