package com.decibeltx.studytracker.events.dto;

import com.decibeltx.studytracker.model.Assay;
import com.decibeltx.studytracker.model.AssayTask;
import com.decibeltx.studytracker.model.Status;
import com.decibeltx.studytracker.model.User;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class AssayView {

  private Long id;

  private Status status;

  private AssayTypeView assayType;

  private String study;

  private String name;

  private String code;

  private String description;

  private String createdBy;

  private String lastModifiedBy;

  private String owner;

  private Date startDate;

  private Date endDate;

  private boolean active;

  private Date createdAt;

  private Date updatedAt;

  private Set<String> users = new HashSet<>();

  private Map<String, Object> fields = new LinkedHashMap<>();

  private Map<String, String> attributes = new LinkedHashMap<>();

  private Set<AssayTask> tasks = new HashSet<>();

  private AssayView() {
  }

  public static AssayView from(Assay assay) {
    AssayView view = new AssayView();
    view.setId(assay.getId());
    view.setName(assay.getName());
    view.setCode(assay.getCode());
    view.setStudy(assay.getStudy().getCode());
    view.setDescription(assay.getDescription());
    view.setStatus(assay.getStatus());
    view.setOwner(assay.getOwner().getDisplayName());
    view.setCreatedBy(assay.getCreatedBy().getDisplayName());
    view.setLastModifiedBy(assay.getLastModifiedBy().getDisplayName());
    view.setCreatedAt(assay.getCreatedAt());
    view.setUpdatedAt(assay.getUpdatedAt());
    view.setActive(assay.isActive());
    view.setStartDate(assay.getStartDate());
    view.setEndDate(assay.getEndDate());
    view.setUsers(assay.getUsers().stream()
        .map(User::getDisplayName)
        .collect(Collectors.toSet()));
    view.setFields(assay.getFields());
    view.setTasks(assay.getTasks());
    view.setAttributes(assay.getAttributes());
    view.setAssayType(AssayTypeView.from(assay.getAssayType()));
    return view;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public AssayTypeView getAssayType() {
    return assayType;
  }

  public void setAssayType(AssayTypeView assayType) {
    this.assayType = assayType;
  }

  public String getStudy() {
    return study;
  }

  public void setStudy(String study) {
    this.study = study;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Set<String> getUsers() {
    return users;
  }

  public void setUsers(Set<String> users) {
    this.users = users;
  }

  public Map<String, Object> getFields() {
    return fields;
  }

  public void setFields(Map<String, Object> fields) {
    this.fields = fields;
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }

  public Set<AssayTask> getTasks() {
    return tasks;
  }

  public void setTasks(Set<AssayTask> tasks) {
    this.tasks = tasks;
  }
}
