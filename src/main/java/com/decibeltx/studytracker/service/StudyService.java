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

package com.decibeltx.studytracker.service;

import com.decibeltx.studytracker.eln.NotebookFolder;
import com.decibeltx.studytracker.eln.StudyNotebookService;
import com.decibeltx.studytracker.exception.DuplicateRecordException;
import com.decibeltx.studytracker.exception.InvalidConstraintException;
import com.decibeltx.studytracker.exception.RecordNotFoundException;
import com.decibeltx.studytracker.model.ELNFolder;
import com.decibeltx.studytracker.model.FileStoreFolder;
import com.decibeltx.studytracker.model.Program;
import com.decibeltx.studytracker.model.Status;
import com.decibeltx.studytracker.model.Study;
import com.decibeltx.studytracker.repository.StudyRepository;
import com.decibeltx.studytracker.storage.StorageFolder;
import com.decibeltx.studytracker.storage.StudyStorageService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Service class for reading and writing {@link Study} records.
 */
@Service
public class StudyService {

  private static final Logger LOGGER = LoggerFactory.getLogger(StudyService.class);

  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private StudyStorageService studyStorageService;

  @Autowired(required = false)
  private StudyNotebookService notebookService;

  @Autowired
  private NamingService namingService;

  /**
   * Finds a single study, identified by its primary key ID
   *
   * @param id pkid
   * @return optional study
   */
  public Optional<Study> findById(Long id) {
    return studyRepository.findById(id);
  }

  /**
   * Returns all study records.
   *
   * @return all studies
   */
  public List<Study> findAll() {
    return studyRepository.findAll();
  }

  /**
   * Finds all studies associated with a given {@link Program}
   *
   * @param program program object
   * @return list of studies
   */
  public List<Study> findByProgram(Program program) {
    return studyRepository.findByProgramId(program.getId());
  }

  /**
   * Finds a study with the unique given name.
   *
   * @param name study name, unique
   * @return optional study
   */
  public List<Study> findByName(String name) {
    return studyRepository.findByName(name);
  }

  /**
   * Finds a study by its unique internal code.
   *
   * @param code internal code
   * @return optional study
   */
  public Optional<Study> findByCode(String code) {
    return studyRepository.findByCode(code);
  }

  /**
   * Finds a study by its optional, unique internal code.
   *
   * @param code internal code
   * @return optional study
   */
  public Optional<Study> findByExternalCode(String code) {
    return studyRepository.findByExternalCode(code);
  }

  /**
   * Creates a new study record
   *
   * @param study new study
   */
  @Transactional
  public void create(Study study) {

    LOGGER.info("Attempting to create new study with name: " + study.getName());

    // Check for existing studies
    if (study.getCode() != null) {
      Optional<Study> optional = studyRepository.findByCode(study.getCode());
      if (optional.isPresent()) {
        throw new DuplicateRecordException("Duplicate study code: " + study.getCode());
      }
    }
    if (studyRepository.findByName(study.getName()).size() > 0) {
      throw new DuplicateRecordException("Duplicate study name: " + study.getName());
    }

    if (study.getCode() == null) {
      study.setCode(namingService.generateStudyCode(study));
    }
    study.setActive(true);

    // External study
    if (study.getCollaborator() != null && StringUtils.isEmpty(study.getExternalCode())) {
      study.setExternalCode(namingService.generateExternalStudyCode(study));
    }

    try {
      studyRepository.save(study);
    } catch (Exception e) {
      if (e instanceof ConstraintViolationException) {
        throw new InvalidConstraintException(e);
      } else {
        throw e;
      }
    }

    // Create the study storage folder
    try {
      studyStorageService.createStudyFolder(study);
      StorageFolder folder = studyStorageService.getStudyFolder(study);
      study.setStorageFolder(FileStoreFolder.from(folder));
      studyRepository.save(study);
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("Failed to create storage folder for study: " + study.getCode());
    }

    // Create the ELN folder
    LOGGER.warn(String.format("Creating ELN entry for study: %s", study.getCode()));
    if (study.isLegacy()) {
      LOGGER.warn(String.format("Legacy Study : %s", study.getCode()));
      ELNFolder notebookFolder = study.getNotebookFolder();
      notebookFolder.setName(namingService.getStudyNotebookFolderName(study));
      study.setNotebookFolder(notebookFolder);
      study.setUpdatedAt(new Date());
      studyRepository.save(study);
    } else {
      if (notebookService != null) {
        try {
          NotebookFolder notebookFolder = notebookService.createStudyFolder(study);
          study.setNotebookFolder(ELNFolder.from(notebookFolder));
          study.setUpdatedAt(new Date());
          studyRepository.save(study);
        } catch (Exception e) {
          e.printStackTrace();
          LOGGER.error("Failed to create notebook entry for study: " + study.getCode());
        }
      }
    }

    LOGGER.info(String.format("Successfully created new study with code %s and ID %s",
        study.getCode(), study.getId()));

  }

  /**
   * Updates an existing study.
   *
   * @param updated existing study
   */
  @Transactional
  public void update(Study updated) {
    LOGGER.info("Attempting to update existing study with code: " + updated.getCode());
    Study study = studyRepository.findById(updated.getId())
        .orElseThrow(RecordNotFoundException::new);

    study.setDescription(updated.getDescription());
    study.setStatus(updated.getStatus());
    study.setStartDate(updated.getStartDate());
    study.setEndDate(updated.getEndDate());
    study.setOwner(updated.getOwner());
    study.setUsers(updated.getUsers());
    study.setKeywords(updated.getKeywords());

    // Collaborator changes
    if (study.getCollaborator() == null && updated.getCollaborator() != null) {
      study.setCollaborator(updated.getCollaborator());
      if (StringUtils.isEmpty(updated.getExternalCode())) {
        study.setExternalCode(namingService.generateExternalStudyCode(study));
      } else {
        study.setExternalCode(updated.getExternalCode());
      }
    } else if (study.getCollaborator() != null && updated.getCollaborator() == null) {
      study.setCollaborator(null);
      study.setExternalCode(null);
    }

    studyRepository.save(study);
  }

  /**
   * Deletes the given study, identifies by its primary key ID.
   *
   * @param study study to be deleted
   */
  @Transactional
  public void delete(Study study) {
    study.setActive(false);
    studyRepository.save(study);
  }

  /**
   * Updates the status of the study with the provided PKID to the provided status.
   *
   * @param study  study
   * @param status status to set
   */
  @Transactional
  public void updateStatus(Study study, Status status) {
    study.setStatus(status);
    if (status.equals(Status.COMPLETE) && study.getEndDate() == null) {
      study.setEndDate(new Date());
    }
    studyRepository.save(study);
  }

  /**
   * Searches the study repository using the provided keyword and returns matching {@link Study}
   * records.
   *
   * @param keyword
   * @return
   */
  @Transactional
  public List<Study> search(String keyword) {
    return studyRepository.findByNameOrCodeLike(keyword);
  }

  /**
   * Counting number of studies created before/after/between given dates.
   */
  public long count() {
    return studyRepository.count();
  }

  public long countFromDate(Date startDate) {
    return studyRepository.countByCreatedAtAfter(startDate);
  }

  public long countBeforeDate(Date endDate) {
    return studyRepository.countByCreatedAtBefore(endDate);
  }

  public long countBetweenDates(Date startDate, Date endDate) {
    return studyRepository.countByCreatedAtBetween(startDate, endDate);
  }

}
