package com.decibeltx.studytracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "assay_tasks")
@EntityListeners(AuditingEntityListener.class)
public class AssayTask extends Task {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assay_id", nullable = false)
  private Assay assay;

  public AssayTask() {
    super();
  }

  public AssayTask(Assay assay, String label) {
    super(label);
    this.assay = assay;
  }

  public AssayTask(Assay assay, String label, TaskStatus status) {
    super(label, status);
    this.assay = assay;
  }

  public AssayTask(Assay assay, String label, TaskStatus status, Integer order) {
    super(label, status, order);
    this.assay = assay;
  }

  @JsonIgnore
  public Assay getAssay() {
    return assay;
  }

  public void setAssay(Assay assay) {
    this.assay = assay;
  }
}
