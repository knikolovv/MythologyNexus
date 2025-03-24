package com.MythologyNexus.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MythologyDTO(Long id, @NotBlank(message = "Mythology name must not be empty!") String name,
                           String description, List<String> characters) {
    public MythologyDTO(Long id, String name) {
        this(id, name, null, null);
    }
}
