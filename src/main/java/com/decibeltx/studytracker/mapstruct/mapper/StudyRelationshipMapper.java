package com.decibeltx.studytracker.mapstruct.mapper;

import com.decibeltx.studytracker.mapstruct.dto.StudyRelationshipDto;
import com.decibeltx.studytracker.model.StudyRelationship;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudyRelationshipMapper {
  StudyRelationship fromDto(StudyRelationshipDto dto);
  List<StudyRelationship> fromDtoList(List<StudyRelationshipDto> dtos);
  Set<StudyRelationship> fromDtoSet(Set<StudyRelationshipDto> dtos);
  StudyRelationshipDto toDto(StudyRelationship relationship);
  List<StudyRelationshipDto> toDtoList(List<StudyRelationship> relationships);
  Set<StudyRelationship> toDtoSet(Set<StudyRelationship> relationships);
}
