package com.MythologyNexus.dto;

import jakarta.validation.constraints.NotBlank;

public record PowerDTO(Long id, @NotBlank(message = "Power name must not be empty!") String name) {
}
