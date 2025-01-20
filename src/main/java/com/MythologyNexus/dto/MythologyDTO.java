package com.MythologyNexus.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MythologyDTO(Long id, String name, String description, List<String> characters) {
    MythologyDTO(Long id,String name) {
        this(id,name,null,null);
    }
}
