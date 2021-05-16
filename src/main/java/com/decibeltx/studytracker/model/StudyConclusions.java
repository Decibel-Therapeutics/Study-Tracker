package com.decibeltx.studytracker.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "study_conclusions")
@Data
@EntityListeners(AuditingEntityListener.class)
public class StudyConclusions extends Conclusions {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "study_id")
  private Study study;

}
