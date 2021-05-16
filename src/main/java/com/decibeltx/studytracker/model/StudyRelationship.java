/*
 * Copyright 2020 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.decibeltx.studytracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "study_relationships")
@Data
public class StudyRelationship {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private Type type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "source_study_id")
  private Study sourceStudy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "target_study_id")
  private Study targetStudy;

  public StudyRelationship() {
  }

  public StudyRelationship(Type type, Study sourceStudy, Study targetStudy) {
    this.type = type;
    this.sourceStudy = sourceStudy;
    this.targetStudy = targetStudy;
  }

  public enum Type {

    IS_RELATED_TO,
    IS_PARENT_OF,
    IS_CHILD_OF,
    IS_BLOCKING,
    IS_BLOCKED_BY,
    IS_PRECEDED_BY,
    IS_SUCCEEDED_BY;

    public static Type getInverse(Type type) {
      switch (type) {
        case IS_RELATED_TO:
          return IS_RELATED_TO;
        case IS_PARENT_OF:
          return IS_CHILD_OF;
        case IS_CHILD_OF:
          return IS_PARENT_OF;
        case IS_BLOCKING:
          return IS_BLOCKED_BY;
        case IS_BLOCKED_BY:
          return IS_BLOCKING;
        case IS_PRECEDED_BY:
          return IS_SUCCEEDED_BY;
        case IS_SUCCEEDED_BY:
          return IS_PRECEDED_BY;
        default:
          return IS_RELATED_TO;
      }
    }

  }

}
