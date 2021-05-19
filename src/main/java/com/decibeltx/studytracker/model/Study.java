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

import com.vladmihalcea.hibernate.type.json.JsonType;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "studies", indexes = {
    @Index(name = "idx_study_code", columnList = "code"),
    @Index(name = "idx_study_name", columnList = "name")
})
@Data
@EntityListeners(AuditingEntityListener.class)
@TypeDef(name = "json", typeClass = JsonType.class)
public class Study {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "code", nullable = false, unique = true)
  @NotNull
  private String code;

  @Column(name = "external_code")
  private String externalCode;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  @NotNull
  private Status status;

  @Column(name = "name", nullable = false)
  @NotNull
  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "program_id")
  private Program program;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  @NotNull
  private String description;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "collaborator_id")
  private Collaborator collaborator;

  @Column(name = "legacy", nullable = false)
  private boolean legacy = false;

  @Column(name = "active", nullable = false)
  private boolean active = true;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "notebook_folder_id")
  private ELNFolder notebookFolder;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "storage_folder_id")
  private FileStoreFolder storageFolder;

  @CreatedBy
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "created_by", nullable = false)
  private User createdBy;

  @LastModifiedBy
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "last_modified_by", nullable = false)
  private User lastModifiedBy;

  @Column(name = "start_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @NotNull
  private Date startDate;

  @Column(name = "end_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date endDate;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "owner", nullable = false)
  private User owner;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "study_users",
      joinColumns = @JoinColumn(name = "study_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  private Set<User> users = new HashSet<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "study_keywords",
      joinColumns = @JoinColumn(name = "study_id"),
      inverseJoinColumns = @JoinColumn(name = "keyword_id"))
  private Set<Keyword> keywords = new HashSet<>();

  @OneToMany(mappedBy = "study", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Assay> assays = new HashSet<>();

  @Type(type = "json")
  @Column(name = "attributes", columnDefinition = "json")
  private Map<String, String> attributes = new LinkedHashMap<>();

  @OneToMany(mappedBy = "study", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ExternalLink> externalLinks = new HashSet<>();

  @OneToMany(mappedBy = "sourceStudy", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<StudyRelationship> studyRelationships = new HashSet<>();

  @OneToOne(mappedBy = "study", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private StudyConclusions conclusions;

  @OneToMany(mappedBy = "study", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Comment> comments = new HashSet<>();

}
