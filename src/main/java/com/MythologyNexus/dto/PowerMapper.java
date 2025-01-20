package com.MythologyNexus.dto;

import com.MythologyNexus.model.Power;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PowerMapper {
    PowerDTO toDto(Power power);
}
