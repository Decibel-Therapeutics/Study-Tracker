package com.decibeltx.studytracker.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "assay_type_tasks")
@EntityListeners(AuditingEntityListener.class)
public class AssayTypeTask extends Task {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assay_type_id", nullable = false)
  private AssayType assayType;

  public AssayTypeTask() {
  }

  public AssayTypeTask(AssayType assayType, String label) {
    super(label);
    this.assayType = assayType;
  }

  public AssayTypeTask(AssayType assayType, String label, TaskStatus status) {
    super(label, status);
    this.assayType = assayType;
  }

  public AssayTypeTask(AssayType assayType, String label, TaskStatus status, Integer order) {
    super(label, status, order);
    this.assayType = assayType;
  }

  public AssayType getAssayType() {
    return assayType;
  }

  public void setAssayType(AssayType assayType) {
    this.assayType = assayType;
  }
}
