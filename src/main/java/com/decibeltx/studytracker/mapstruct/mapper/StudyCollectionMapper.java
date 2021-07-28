package com.decibeltx.studytracker.mapstruct.mapper;

import com.decibeltx.studytracker.mapstruct.dto.StudyCollectionDto;
import com.decibeltx.studytracker.model.StudyCollection;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudyCollectionMapper {

  StudyCollection fromDto(StudyCollectionDto dto);
  List<StudyCollection> fromDtoList(List<StudyCollection> dtos);
  StudyCollectionDto toDto(StudyCollection collection);
  List<StudyCollectionDto> toDtoList(List<StudyCollection> collections);

}
