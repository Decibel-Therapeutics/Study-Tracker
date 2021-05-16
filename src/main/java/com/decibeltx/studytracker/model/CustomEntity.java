package com.decibeltx.studytracker.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class CustomEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name", unique = true, nullable = false)
  @NotNull
  private String name;

  @Column(name = "description", nullable = false)
  @NotNull
  private String description;

  @Column(name = "active", nullable = false)
  private boolean active;

}
