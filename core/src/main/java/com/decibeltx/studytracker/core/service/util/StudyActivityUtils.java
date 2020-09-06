package com.decibeltx.studytracker.core.service.util;

import com.decibeltx.studytracker.core.model.Activity;
import com.decibeltx.studytracker.core.model.Activity.Reference;
import com.decibeltx.studytracker.core.model.Comment;
import com.decibeltx.studytracker.core.model.Conclusions;
import com.decibeltx.studytracker.core.model.EventType;
import com.decibeltx.studytracker.core.model.ExternalLink;
import com.decibeltx.studytracker.core.model.Status;
import com.decibeltx.studytracker.core.model.Study;
import com.decibeltx.studytracker.core.model.StudyRelationship;
import com.decibeltx.studytracker.core.model.User;
import com.decibeltx.studytracker.core.storage.StorageFile;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StudyActivityUtils {

  public static Activity fromNewStudy(Study study, User triggeredBy) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.NEW_STUDY);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("program", study.getProgram().getName());
    data.put("code", study.getCode());
    data.put("externalCode", study.getExternalCode());
    data.put("createdAt", study.getCreatedAt());
    data.put("createdBy", study.getCreatedBy().getUsername());
    data.put("description", study.getDescription());
    data.put("name", study.getName());
    data.put("status", study.getStatus());
    data.put("keywords", study.getKeywords());
    activity.setData(data);
    return activity;
  }

  public static Activity fromUpdatedStudy(Study study, User triggeredBy) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.UPDATED_STUDY);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("program", study.getProgram().getName());
    data.put("code", study.getCode());
    data.put("externalCode", study.getExternalCode());
    data.put("createdAt", study.getCreatedAt());
    data.put("createdBy", study.getCreatedBy().getUsername());
    data.put("updatedAt", study.getUpdatedAt());
    data.put("lastModifiedBy", study.getLastModifiedBy().getUsername());
    data.put("description", study.getDescription());
    data.put("name", study.getName());
    data.put("status", study.getStatus());
    data.put("keywords", study.getKeywords());
    activity.setData(data);
    return activity;
  }

  public static Activity fromDeletedStudy(Study study, User triggeredBy) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.DELETED_STUDY);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    return activity;
  }

  public static Activity fromStudyStatusChange(Study study, User triggeredBy, Status oldStatus,
      Status newStatus) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.STUDY_STATUS_CHANGED);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("code", study.getCode());
    data.put("name", study.getName());
    data.put("oldStatus", oldStatus);
    data.put("newStatus", newStatus);
    activity.setData(data);
    return activity;
  }

  public static Activity fromFileUpload(Study study, User triggeredBy, StorageFile storageFile) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.FILE_UPLOADED);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("code", study.getCode());
    data.put("name", study.getName());
    data.put("fileName", storageFile.getName());
    data.put("filePath", storageFile.getPath());
    data.put("url", storageFile.getUrl());
    activity.setData(data);
    return activity;
  }

  public static Activity fromNewConclusions(Study study, User triggeredBy,
      Conclusions conclusions) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.NEW_STUDY_CONCLUSIONS);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("code", study.getCode());
    data.put("name", study.getName());
    data.put("content", conclusions.getContent());
    data.put("createdBy", conclusions.getCreatedBy().getUsername());
    data.put("createdAt", conclusions.getCreatedAt());
    activity.setData(data);
    return activity;
  }

  public static Activity fromUpdatedConclusions(Study study, User triggeredBy,
      Conclusions conclusions) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.EDITED_STUDY_CONCLUSIONS);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("code", study.getCode());
    data.put("name", study.getName());
    data.put("content", conclusions.getContent());
    data.put("createdBy", conclusions.getCreatedBy().getUsername());
    data.put("createdAt", conclusions.getCreatedAt());
    data.put("lastModifiedBy", conclusions.getLastModifiedBy().getUsername());
    data.put("updatedAt", conclusions.getUpdatedAt());
    activity.setData(data);
    return activity;
  }

  public static Activity fromDeletedConclusions(Study study, User triggeredBy) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.DELETED_STUDY_CONCLUSIONS);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("code", study.getCode());
    data.put("name", study.getName());
    activity.setData(data);
    return activity;
  }

  public static Activity fromNewComment(Study study, User triggeredBy, Comment comment) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.NEW_COMMENT);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("code", study.getCode());
    data.put("name", study.getName());
    data.put("text", comment.getText());
    data.put("createdBy", comment.getCreatedBy().getUsername());
    data.put("createdAt", comment.getCreatedAt());
    activity.setData(data);
    return activity;
  }

  public static Activity fromEditiedComment(Study study, User triggeredBy, Comment comment) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.EDITED_COMMENT);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("code", study.getCode());
    data.put("name", study.getName());
    data.put("text", comment.getText());
    data.put("createdBy", comment.getCreatedBy().getUsername());
    data.put("createdAt", comment.getCreatedAt());
    data.put("updatedAt", comment.getUpdatedAt());
    activity.setData(data);
    return activity;
  }

  public static Activity fromDeletedComment(Study study, User triggeredBy) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.DELETED_COMMENT);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("code", study.getCode());
    data.put("name", study.getName());
    activity.setData(data);
    return activity;
  }

  public static Activity fromNewStudyRelationship(Study study, User triggeredBy,
      StudyRelationship relationship) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.NEW_STUDY_RELATIONSHIP);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("targetStudyCode", relationship.getStudy().getCode());
    data.put("targetStudyName", relationship.getStudy().getName());
    data.put("sourceStudyCode", study.getCode());
    data.put("sourceStudyName", study.getName());
    data.put("type", relationship.getType());
    activity.setData(data);
    return activity;
  }

  public static Activity fromUpdatedStudyRelationship(Study study, User triggeredBy,
      StudyRelationship relationship) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.UPDATED_STUDY_RELATIONSHIP);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("targetStudyCode", relationship.getStudy().getCode());
    data.put("targetStudyName", relationship.getStudy().getName());
    data.put("sourceStudyCode", study.getCode());
    data.put("sourceStudyName", study.getName());
    data.put("type", relationship.getType());
    activity.setData(data);
    return activity;
  }

  public static Activity fromDeletedStudyRelationship(Study study, User triggeredBy) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.DELETED_STUDY_RELATIONSHIP);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("code", study.getCode());
    data.put("name", study.getName());
    activity.setData(data);
    return activity;
  }

  public static Activity fromNewExternalLink(Study study, User triggeredBy, ExternalLink link) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.NEW_STUDY_EXTERNAL_LINK);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("code", study.getCode());
    data.put("name", study.getName());
    data.put("label", link.getLabel());
    data.put("url", link.getUrl());
    activity.setData(data);
    return activity;
  }

  public static Activity fromUpdatedExternalLink(Study study, User triggeredBy, ExternalLink link) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.UPDATED_STUDY_EXTERNAL_LINK);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("code", study.getCode());
    data.put("name", study.getName());
    data.put("label", link.getLabel());
    data.put("url", link.getUrl());
    activity.setData(data);
    return activity;
  }

  public static Activity fromDeletedExternalLink(Study study, User triggeredBy) {
    Activity activity = new Activity();
    activity.setReference(Reference.STUDY);
    activity.setReferenceId(study.getId());
    activity.setEventType(EventType.DELETED_STUDY_EXTERNAL_LINK);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("code", study.getCode());
    data.put("name", study.getName());
    activity.setData(data);
    return activity;
  }

}
