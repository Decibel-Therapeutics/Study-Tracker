package com.decibeltx.studytracker.mapstruct.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class ProgramDetailsDto {

  private Long id;
  private String code;
  private String name;
  private String description;
  private UserSummaryDto createdBy;
  private UserSummaryDto lastModifiedBy;
  private Date createdAt;
  private Date updatedAt;
  private boolean active;
  private ELNFolderDto notebookFolder;
  private FileStoreFolderDto storageFolder;
  private Map<String, String> attributes = new HashMap<>();

}
