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

import com.decibeltx.studytracker.eln.NotebookEntry;
import com.decibeltx.studytracker.eln.NotebookFolder;
import com.decibeltx.studytracker.eln.NotebookTemplate;
import com.decibeltx.studytracker.eln.StudyNotebookService;
import com.decibeltx.studytracker.exception.DuplicateRecordException;
import com.decibeltx.studytracker.exception.InvalidConstraintException;
import com.decibeltx.studytracker.exception.RecordNotFoundException;
import com.decibeltx.studytracker.exception.StudyTrackerException;
import com.decibeltx.studytracker.model.ELNFolder;
import com.decibeltx.studytracker.model.ExternalLink;
import com.decibeltx.studytracker.model.FileStoreFolder;
import com.decibeltx.studytracker.model.Program;
import com.decibeltx.studytracker.model.Status;
import com.decibeltx.studytracker.model.Study;
import com.decibeltx.studytracker.model.User;
import com.decibeltx.studytracker.repository.ELNFolderRepository;
import com.decibeltx.studytracker.repository.FileStoreFolderRepository;
import com.decibeltx.studytracker.repository.ProgramRepository;
import com.decibeltx.studytracker.repository.StudyRepository;
import com.decibeltx.studytracker.storage.StorageFolder;
import com.decibeltx.studytracker.storage.StudyStorageService;
import com.decibeltx.studytracker.storage.exception.StudyStorageNotFoundException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  private ProgramRepository programRepository;

  @Autowired
  private StudyStorageService studyStorageService;

  @Autowired(required = false)
  private StudyNotebookService notebookService;

  @Autowired
  private NamingService namingService;

  @Autowired
  private FileStoreFolderRepository fileStoreFolderRepository;

  @Autowired
  private ELNFolderRepository elnFolderRepository;

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
   * Returns a {@link Page} of studies
   *
   * @param pageable
   * @return
   */
  public Page<Study> findAll(Pageable pageable) {
    return studyRepository.findAll(pageable);
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

  public List<Study> findByUser(User user) {
    return studyRepository.findByUsersId(user.getId());
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

  public void create(Study study) {
    this.create(study, null);
  }

  /**
   * Creates a new study record, creates a storage folder, creates and ELN folder, and creates
   *  an ELN entry for the study.
   *
   * @param study new study
   */
  @Transactional
  public void create(Study study, NotebookTemplate template) {

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

    // Assign the code, if necessary
    if (study.getCode() == null) {
      study.setCode(namingService.generateStudyCode(study));
    }
    study.setActive(true);

    // External study
    if (study.getCollaborator() != null && StringUtils.isEmpty(study.getExternalCode())) {
      study.setExternalCode(namingService.generateExternalStudyCode(study));
    }

    // Get the program
    Program program = programRepository.findById(study.getProgram().getId())
        .orElseThrow(() -> new RecordNotFoundException("Invalid program: " + study.getProgram().getId()));

    // Create the study storage folder
    try {
      studyStorageService.createStudyFolder(study);
      StorageFolder folder = studyStorageService.getStudyFolder(study);
      study.setStorageFolder(FileStoreFolder.from(folder));
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.warn("Failed to create storage folder for study: " + study.getCode());
    }

    // Create the ELN folder
    ELNFolder elnFolder = null;
    NotebookEntry studySummaryEntry = null;
    if (study.isLegacy()) {
      LOGGER.info(String.format("Legacy Study : %s", study.getCode()));
      if (study.getNotebookFolder().getUrl() != null) {
        elnFolder = study.getNotebookFolder();
        elnFolder.setName(namingService.getStudyNotebookFolderName(study));
        study.setNotebookFolder(elnFolder);
      } else {
        LOGGER.warn("No ELN URL set, so folder reference will not be created.");
        study.setNotebookFolder(null);
      }
    } else {
      // New study and notebook integration active
      if (notebookService != null) {
        LOGGER.info(String.format("Creating ELN entry for study: %s", study.getCode()));
        if (program.getNotebookFolder() != null) {
          try {

            // Create the notebook folder
            NotebookFolder notebookFolder = notebookService.createStudyFolder(study);
            elnFolder = ELNFolder.from(notebookFolder);
            study.setNotebookFolder(elnFolder);

            // Creat the notebook entry
            studySummaryEntry = notebookService.createStudyNotebookEntry(study, template);

          } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("Failed to create notebook folder and entry for study: " + study.getCode());

          }
        } else {
          LOGGER.warn(String.format("Study program %s does not have ELN folder set.", program.getName()));
        }
      } else {
        study.setNotebookFolder(null);
      }
    }

    // Persist the record
    try {
      studyRepository.save(study);
      LOGGER.info(String.format("Successfully created new study with code %s and ID %s",
          study.getCode(), study.getId()));
    } catch (Exception e) {
      if (e instanceof ConstraintViolationException) {
        throw new InvalidConstraintException(e);
      } else {
        throw e;
      }
    }

    // Add a link to the study summary entry
    if (studySummaryEntry != null) {
      try {
        ExternalLink entryLink = new ExternalLink();
        entryLink.setStudy(study);
        entryLink.setLabel("Summary ELN Entry");
        entryLink.setUrl(new URL(studySummaryEntry.getUrl()));
        study.addExternalLink(entryLink);
        studyRepository.save(study);
      } catch (Exception e) {
        e.printStackTrace();
        LOGGER.warn("Failed to create link to ELN entry.");
      }
    }

  }

  /**
   * Updates an existing study.
   *
   * @param updated existing study
   */
  @Transactional
  public void update(Study updated) {
    LOGGER.info("Attempting to update existing study with code: " + updated.getCode());
    Study study = studyRepository.getById(updated.getId());

    study.setDescription(updated.getDescription());
    study.setExternalCode(updated.getExternalCode());
    study.setStatus(updated.getStatus());
    study.setStartDate(updated.getStartDate());
    study.setEndDate(updated.getEndDate());
    study.setOwner(updated.getOwner());
    study.setUsers(updated.getUsers());
    study.setKeywords(updated.getKeywords());
    study.setAttributes(updated.getAttributes());

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
    Study s = studyRepository.getById(study.getId());
    s.setActive(false);
    studyRepository.save(s);
  }

  /**
   * Updates the status of the study with the provided PKID to the provided status.
   *
   * @param study  study
   * @param status status to set
   */
  @Transactional
  public void updateStatus(Study study, Status status) {
    Study s = studyRepository.getById(study.getId());
    s.setStatus(status);
    if (status.equals(Status.COMPLETE) && study.getEndDate() == null) {
      s.setEndDate(new Date());
    }
    studyRepository.save(s);
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
   * Checks to see whether the study with the provided ID exists.
   *
   * @param studyId
   * @return
   */
  public boolean exists(Long studyId) {
    return studyRepository.existsById(studyId);
  }

  /**
   * Manually updates a study's {@code updatedAt} and {@code lastModifiedBy} fields.
   *
   * @param study
   * @param user
   */
  @Transactional
  public void markAsUpdated(Study study, User user) {
    Study s = studyRepository.getById(study.getId());
    s.setLastModifiedBy(user);
    s.setUpdatedAt(new Date());
    studyRepository.save(s);
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

  public long countUserActiveStudies(User user) {
    return studyRepository.countActiveUserStudies(user.getId());
  }

  public long countUserCompleteStudies(User user) {
    return studyRepository.countCompleteUserStudies(user.getId());
  }

  public long countByProgram(Program program) {
    return studyRepository.countByProgram(program);
  }

  public long countByProgramAfterDate(Program program, Date date) {
    return studyRepository.countByProgramAndCreatedAtAfter(program, date);
  }

  @Transactional
  public void repairStorageFolder(Study study) {

    // Find or create the storage folder
    StorageFolder folder;
    try {
      folder = studyStorageService.getStudyFolder(study, false);
    } catch (StudyStorageNotFoundException e) {
      try {
        folder = studyStorageService.createStudyFolder(study);
      } catch (Exception ex) {
        throw new StudyTrackerException(ex);
      }
    }

    // Update the  program record
    FileStoreFolder f = fileStoreFolderRepository.getById(study.getStorageFolder().getId());
    f.setName(folder.getName());
    f.setPath(folder.getPath());
    f.setUrl(folder.getUrl());
    fileStoreFolderRepository.save(f);
  }

  @Transactional
  public void repairElnFolder(Study study) {

    // Check to see if the folder exists and create a new one if necessary
    NotebookFolder folder;
    Optional<NotebookFolder> optional = notebookService.findStudyFolder(study);
    if (optional.isPresent()) {
      folder = optional.get();
    } else {
      folder = notebookService.createStudyFolder(study);
    }

    // Update the record
    ELNFolder f;
    boolean isNew = false;
    try {
      f = elnFolderRepository.getById(study.getNotebookFolder().getId());
    } catch (NullPointerException e) {
      f = new ELNFolder();
      isNew = true;
    }
    f.setName(folder.getName());
    f.setPath(folder.getPath());
    f.setUrl(folder.getUrl());
    f.setReferenceId(folder.getReferenceId());
    elnFolderRepository.save(f);

    if (isNew) {
      Study s = studyRepository.getById(study.getId());
      s.setNotebookFolder(f);
      studyRepository.save(s);
    }

  }

}
