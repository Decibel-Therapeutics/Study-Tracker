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
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "collaborators")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Collaborator {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "label", unique = true, nullable = false)
  @NotNull
  private String label;

  @Column(name = "organization_name", nullable = false)
  @NotNull
  private String organizationName;

  @Column(name = "organization_location")
  private String organizationLocation;

  @Column(name = "contact_person_name")
  private String contactPersonName;

  @Column(name = "contact_email")
  private String contactEmail;

  @Column(name = "code", nullable = false)
  @NotNull
  private String code;

  @Column(name = "active", nullable = false)
  private boolean active = true;

}
