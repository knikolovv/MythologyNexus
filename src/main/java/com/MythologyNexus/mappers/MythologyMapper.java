package com.MythologyNexus.mappers;

import com.MythologyNexus.dto.MythologyDTO;
import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.Mythology;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MythologyMapper {
    @Mapping(target = "characters", source = "characters")
    MythologyDTO toDto(Mythology mythology);

    default List<String> mapCharacterNames(List<Character> Characters) {
        if (Characters == null) {
            return null;
        }
        return Characters.stream()
                .map(Character::getName)
                .collect(Collectors.toList());
    }
}
