package com.decibeltx.studytracker.model;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class CustomEntityField {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "display_name", nullable = false)
  private String displayName;

  @Column(name = "field_name", nullable = false)
  private String fieldName;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private CustomEntityFieldType type;

  @Column(name = "required", nullable = false)
  private boolean required;

  @Column(name = "description")
  private String description;

  @Column(name = "active", nullable = false)
  private boolean active;

  public enum CustomEntityFieldType {
    STRING,
    TEXT,
    INTEGER,
    FLOAT,
    DATE,
    BOOLEAN
  }

}
