package com.decibeltx.studytracker.mapstruct.dto;

import com.decibeltx.studytracker.model.RelationshipType;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudyRelationshipDto {

  private Long id;
  @NotNull private RelationshipType type;
  private StudySlimDto sourceStudy;
  private StudySlimDto targetRelationship;

}
