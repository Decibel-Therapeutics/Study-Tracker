package com.decibeltx.studytracker.events.dto;

import com.decibeltx.studytracker.model.AssayType;
import com.decibeltx.studytracker.model.AssayTypeField;
import com.decibeltx.studytracker.model.AssayTypeTask;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class AssayTypeView {

  private Long id;

  private String name;

  private boolean active;

  private Set<AssayTypeField> fields = new HashSet<>();

  private Set<AssayTypeTask> tasks = new HashSet<>();

  private Map<String, String> attributes = new HashMap<>();

  private AssayTypeView() {
  }

  public static AssayTypeView from(AssayType assayType) {
    AssayTypeView view = new AssayTypeView();
    view.setId(assayType.getId());
    view.setName(assayType.getName());
    view.setActive(assayType.isActive());
    view.setFields(assayType.getFields());
    view.setTasks(assayType.getTasks());
    view.setAttributes(assayType.getAttributes());
    return view;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Set<AssayTypeField> getFields() {
    return fields;
  }

  public void setFields(Set<AssayTypeField> fields) {
    this.fields = fields;
  }

  public Set<AssayTypeTask> getTasks() {
    return tasks;
  }

  public void setTasks(Set<AssayTypeTask> tasks) {
    this.tasks = tasks;
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }
}
