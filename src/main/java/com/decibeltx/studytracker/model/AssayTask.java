package com.decibeltx.studytracker.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "assay_tasks")
@EntityListeners(AuditingEntityListener.class)
@Data
public class AssayTask extends Task {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assay_id")
  private Assay assay;

  public AssayTask() {
  }

  public AssayTask(String label) {
    super(label);
  }

  public AssayTask(String label, TaskStatus status) {
    super(label, status);
  }

  public AssayTask(String label, TaskStatus status, Integer order) {
    super(label, status, order);
  }
}
