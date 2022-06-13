package io.studytracker.mapstruct.mapper;

import io.studytracker.mapstruct.dto.KeywordDto;
import io.studytracker.model.Keyword;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KeywordMapper {
  KeywordDto toDto(Keyword keyword);

  List<KeywordDto> toDtoList(List<Keyword> keywords);

  Set<KeywordDto> toDtoSet(Set<Keyword> keywords);

  Keyword fromDto(KeywordDto dto);

  List<Keyword> fromDtoList(List<KeywordDto> dtos);

  Set<Keyword> fromDtoSet(Set<KeywordDto> dtos);
}