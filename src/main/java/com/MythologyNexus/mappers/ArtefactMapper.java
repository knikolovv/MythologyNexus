package com.MythologyNexus.mappers;

import com.MythologyNexus.dto.ArtefactDTO;
import com.MythologyNexus.model.Artefact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtefactMapper {
    ArtefactDTO toDto(Artefact artefact);

}
