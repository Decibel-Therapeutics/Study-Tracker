package com.decibeltx.studytracker.mapstruct.dto;

import lombok.Data;

@Data
public class UserSummaryDto {

  private Long id;
  private String username;
  private String department;
  private String title;
  private String displayName;
  private String email;
  private boolean admin;

}
