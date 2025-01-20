package com.MythologyNexus.dto;

import com.MythologyNexus.model.Character;
import com.MythologyNexus.model.Mythology;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@Component
public interface CharacterMapper {
    @Mapping(target = "associatedCharacterNames", source = "associatedCharacters")
    CharacterDTO toDto(Character character);

    default MythologyDTO mapMythology(Mythology mythology) {
        if (mythology == null) {
            return null;
        }
        return new MythologyDTO(mythology.getId(), mythology.getName());
    }

    default List<String> mapAssociatedCharacterNames(List<Character> associatedCharacters) {
        if (associatedCharacters == null) {
            return null;
        }
        return associatedCharacters.stream()
                .map(Character::getName)
                .collect(Collectors.toList());
    }


}
