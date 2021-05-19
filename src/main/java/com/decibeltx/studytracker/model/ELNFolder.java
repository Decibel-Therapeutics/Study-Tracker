package com.decibeltx.studytracker.model;

import com.decibeltx.studytracker.eln.NotebookFolder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "eln_folders")
@Data
public class ELNFolder {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "url", nullable = false, length = 1024)
  private String url;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "path")
  private String path;

  @Column(name = "reference_id")
  private String referenceId;

  public static ELNFolder from(NotebookFolder notebookFolder) {
    ELNFolder f = new ELNFolder();
    f.setName(notebookFolder.getName());
    f.setPath(notebookFolder.getPath());
    f.setReferenceId(notebookFolder.getReferenceId());
    f.setUrl(notebookFolder.getUrl());
    return f;
  }

}
