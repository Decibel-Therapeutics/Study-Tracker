package com.decibeltx.studytracker.events.util;

import com.decibeltx.studytracker.model.Assay;
import com.decibeltx.studytracker.model.AssayType;
import com.decibeltx.studytracker.model.Comment;
import com.decibeltx.studytracker.model.Keyword;
import com.decibeltx.studytracker.model.Program;
import com.decibeltx.studytracker.model.Study;
import com.decibeltx.studytracker.model.StudyConclusions;
import com.decibeltx.studytracker.model.StudyRelationship;
import com.decibeltx.studytracker.model.User;
import com.decibeltx.studytracker.storage.StorageFile;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityViewUtils {

  public static Map<String, Object> createProgramView(Program program) {
    Map<String, Object> view = new HashMap<>();
    view.put("id", program.getId());
    view.put("name", program.getName());
    view.put("code", program.getCode());
    view.put("createdAt", program.getCreatedAt());
    view.put("updatedAt", program.getUpdatedAt());
    view.put("createdBy", program.getCreatedBy().getDisplayName());
    view.put("description", program.getDescription());
    view.put("attributes", program.getAttributes());
    return view;
  }

  public static Map<String, Object> createStudyView(Study study) {
    Map<String, Object> view = new HashMap<>();
    view.put("id", study.getId());
    view.put("name", study.getName());
    view.put("code", study.getCode());
    view.put("externalCode", study.getExternalCode());
    view.put("program", study.getProgram().getName());
    view.put("description", study.getDescription());
    view.put("status", study.getStatus().toString());
    view.put("owner", study.getOwner().getDisplayName());
    view.put("createdBy", study.getCreatedBy().getDisplayName());
    view.put("lastModifiedBy", study.getLastModifiedBy().getDisplayName());
    view.put("createdAt", study.getCreatedAt());
    view.put("updatedAt", study.getUpdatedAt());
    view.put("legacy", study.isLegacy());
    view.put("active", study.isActive());
    view.put("keyword", study.getKeywords().stream()
        .map(EntityViewUtils::createKeywordView)
        .collect(Collectors.toSet()));
    view.put("startDate", study.getStartDate());
    view.put("endDate", study.getEndDate());
    view.put("users", study.getUsers().stream()
        .map(User::getDisplayName)
        .collect(Collectors.toSet()));
    view.put("attributes", study.getAttributes());
    if (study.getCollaborator() != null) {
      view.put("collaborator", study.getCollaborator().getLabel());
    }
    return view;
  }

  public static Map<String, Object> createStudyRelationshipView(StudyRelationship relationship) {
    Map<String, Object> view = new HashMap<>();
    view.put("type", relationship.getType());
    view.put("id", relationship.getId());
    return view;
  }

  public static Map<String, Object> createStorageFileView(StorageFile file) {
    Map<String, Object> view = new HashMap<>();
    view.put("name", file.getName());
    view.put("path", file.getPath());
    view.put("url", file.getUrl());
    return view;
  }

  public static Map<String, Object> createKeywordView(Keyword keyword) {
    Map<String, Object> view = new HashMap<>();
    view.put("id", keyword.getId());
    view.put("category", keyword.getKeyword());
    view.put("keyword", keyword.getKeyword());
    return view;
  }

  public static Map<String, Object> createStudyConclusionsView(StudyConclusions conclusions) {
    Map<String, Object> view = new HashMap<>();
    view.put("id", conclusions.getId());
    view.put("content", conclusions.getContent());
    view.put("date", conclusions.getUpdatedAt() != null
        ? conclusions.getUpdatedAt() : conclusions.getCreatedAt());
    view.put("user", conclusions.getLastModifiedBy() != null
        ? conclusions.getLastModifiedBy().getDisplayName()
        : conclusions.getCreatedBy().getDisplayName());
    return view;
  }

  public static Map<String, Object> createCommentView(Comment comment) {
    Map<String, Object> view = new HashMap<>();
    view.put("id", comment.getId());
    view.put("text", comment.getText());
    view.put("date", comment.getUpdatedAt() != null ? comment.getUpdatedAt() : comment.getCreatedAt());
    view.put("user", comment.getCreatedBy().getDisplayName());
    return view;
  }

  public static Map<String, Object> createAssayView(Assay assay) {
    Map<String, Object> view = new HashMap<>();
    view.put("id", assay.getId());
    view.put("name", assay.getName());
    view.put("code", assay.getCode());
    view.put("study", assay.getStudy().getCode());
    view.put("description", assay.getDescription());
    view.put("status", assay.getStatus().toString());
    view.put("owner", assay.getOwner().getDisplayName());
    view.put("createdBy", assay.getCreatedBy().getDisplayName());
    view.put("lastModifiedBy", assay.getLastModifiedBy().getDisplayName());
    view.put("createdAt", assay.getCreatedAt());
    view.put("updatedAt", assay.getUpdatedAt());
    view.put("active", assay.isActive());
    view.put("startDate", assay.getStartDate());
    view.put("endDate", assay.getEndDate());
    view.put("users", assay.getUsers().stream()
        .map(User::getDisplayName)
        .collect(Collectors.toSet()));
    view.put("fields", assay.getFields());
    view.put("tasks", assay.getTasks());
    view.put("attributes", assay.getAttributes());
    view.put("assayType", createAssayTypeView(assay.getAssayType()));
    return view;
  }

  public static Map<String, Object> createAssayTypeView(AssayType assayType) {
    Map<String, Object> view = new HashMap<>();
    view.put("id", assayType.getId());
    view.put("name", assayType.getName());
    view.put("active", assayType.isActive());
    view.put("fields", assayType.getFields());
    view.put("tasks", assayType.getTasks());
    view.put("attributes", assayType.getAttributes());
    return view;
  }

}
