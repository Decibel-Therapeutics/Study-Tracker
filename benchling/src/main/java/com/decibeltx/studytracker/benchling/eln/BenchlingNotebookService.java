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

package com.decibeltx.studytracker.benchling.eln;

import com.decibeltx.studytracker.benchling.eln.entities.BenchlingFolder;
import com.decibeltx.studytracker.benchling.exception.BenchlingException;
import com.decibeltx.studytracker.benchling.exception.EntityNotFoundException;
import com.decibeltx.studytracker.core.eln.NotebookFolder;
import com.decibeltx.studytracker.core.eln.NotebookUtils;
import com.decibeltx.studytracker.core.eln.StudyNotebookService;
import com.decibeltx.studytracker.core.exception.NotebookException;
import com.decibeltx.studytracker.core.model.Assay;
import com.decibeltx.studytracker.core.model.Program;
import com.decibeltx.studytracker.core.model.Study;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public final class BenchlingNotebookService implements StudyNotebookService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BenchlingNotebookService.class);


  @Autowired
  private BenchlingElnRestClient client;

  @Autowired
  private BenchlingElnOptions options;

  private String createFolderUrl(BenchlingFolder folder) {
    return options.getRootFolderUrl() + "/" + folder.getId().replace("lib_", "") + "-"
        + folder.getName().toLowerCase()
        .replaceAll(" ", "-")
        .replaceAll("[^A-Za-z0-9-_\\s()]+", "")
        .replaceAll("[\\()]", "")
        .trim();
  }

  private NotebookFolder convertFolder(BenchlingFolder folder) {
    NotebookFolder notebookFolder = new NotebookFolder();
    notebookFolder.setName(folder.getName());
    notebookFolder.setReferenceId(folder.getId());
    notebookFolder.setUrl(this.createFolderUrl(folder));
    notebookFolder.getAttributes().put("projectId", folder.getProjectId());
    return notebookFolder;
  }

  @Override
  public Optional<NotebookFolder> findProgramFolder(Program program) {
    LOGGER.info("Fetching benchling notebook entry for program: " + program.getName());
    NotebookFolder notebookFolder = null;
    String folderName = NotebookUtils.getProgramFolderName(program);
    Optional<BenchlingFolder> optional = client.findRootFolders().stream()
        .filter(f -> f.getName().equals(folderName))
        .findFirst();
    if (optional.isPresent()) {
      notebookFolder = this.convertFolder(optional.get());
    }
    return Optional.ofNullable(notebookFolder);
  }

  @Override
  public Optional<NotebookFolder> findStudyFolder(Study study) {
    LOGGER.info("Fetching notebook entry for study: " + study.getCode());
    Optional<NotebookFolder> programFolderOptional = this.findProgramFolder(study.getProgram());
    if (!programFolderOptional.isPresent()) {
      throw new EntityNotFoundException("Cannot find program folder for study: " + study.getCode());
    }
    NotebookFolder programFolder = programFolderOptional.get();
    NotebookFolder notebookFolder = null;

    Optional<BenchlingFolder> optional = client.findFolderChildren(programFolder.getReferenceId())
        .stream()
        .filter(f -> f.getName().equals(NotebookUtils.getStudyFolderName(study)))
        .findFirst();
    if (optional.isPresent()) {
      notebookFolder = this.convertFolder(optional.get());
      notebookFolder.setParentFolder(programFolder);
    }
    return Optional.ofNullable(notebookFolder);
  }

  @Override
  public Optional<NotebookFolder> findAssayFolder(Assay assay) {
    LOGGER.info("Fetching notebook entry for assay: " + assay.getCode());
    Optional<NotebookFolder> studyFolderOptional = this.findStudyFolder(assay.getStudy());
    if (!studyFolderOptional.isPresent()) {
      throw new EntityNotFoundException("Cannot find study folder for assay: " + assay.getCode());
    }
    NotebookFolder studyFolder = studyFolderOptional.get();
    NotebookFolder notebookFolder = null;

    Optional<BenchlingFolder> optional = client.findFolderChildren(studyFolder.getReferenceId())
        .stream()
        .filter(f -> f.getName().equals(NotebookUtils.getAssayFolderName(assay)))
        .findFirst();
    if (optional.isPresent()) {
      notebookFolder = this.convertFolder(optional.get());
      notebookFolder.setParentFolder(studyFolder);
    }
    return Optional.ofNullable(notebookFolder);
  }

  @Override
  public NotebookFolder createProgramFolder(Program program) throws NotebookException {
    LOGGER.info("Method not implemented.");
    throw new BenchlingException(
        "Benchling does not support creating project folders via the API.");
  }

  @Override
  public NotebookFolder createStudyFolder(Study study) throws NotebookException {
    LOGGER.info("Creating Benchling folder for study: " + study.getCode());

    Optional<NotebookFolder> programFolderOptional = this.findProgramFolder(study.getProgram());
    if (!programFolderOptional.isPresent()) {
      throw new EntityNotFoundException(
          "Could not find folder for program: " + study.getProgram().getName());
    }
    NotebookFolder programFolder = programFolderOptional.get();

    BenchlingFolder benchlingFolder = client
        .createFolder(NotebookUtils.getStudyFolderName(study), programFolder.getReferenceId());
    NotebookFolder studyFolder = this.convertFolder(benchlingFolder);
    studyFolder.setParentFolder(programFolder);
    return studyFolder;

  }


  @Override
  public NotebookFolder createAssayFolder(Assay assay) throws NotebookException {
    LOGGER.info("Creating Benchling folder for assay: " + assay.getCode());

    Optional<NotebookFolder> studyFolderOptional = this.findStudyFolder(assay.getStudy());
    if (!studyFolderOptional.isPresent()) {
      throw new EntityNotFoundException(
          "Could not find folder for study: " + assay.getStudy().getCode());
    }
    NotebookFolder studyFolder = studyFolderOptional.get();

    BenchlingFolder benchlingFolder = client
        .createFolder(NotebookUtils.getAssayFolderName(assay), studyFolder.getReferenceId());
    NotebookFolder assayFolder = this.convertFolder(benchlingFolder);
    assayFolder.setParentFolder(studyFolder);

    return assayFolder;

  }
}
