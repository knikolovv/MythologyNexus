package com.MythologyNexus.mappers;

import com.MythologyNexus.dto.PowerDTO;
import com.MythologyNexus.model.Power;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PowerMapper {
    PowerDTO toDto(Power power);
}
