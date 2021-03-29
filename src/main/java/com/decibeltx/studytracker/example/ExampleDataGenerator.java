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

package com.decibeltx.studytracker.example;

import com.decibeltx.studytracker.eln.NotebookFolder;
import com.decibeltx.studytracker.events.util.EntryTemplateActivityUtils;
import com.decibeltx.studytracker.events.util.StudyActivityUtils;
import com.decibeltx.studytracker.exception.RecordNotFoundException;
import com.decibeltx.studytracker.exception.StudyTrackerException;
import com.decibeltx.studytracker.model.Activity;
import com.decibeltx.studytracker.model.Assay;
import com.decibeltx.studytracker.model.AssayType;
import com.decibeltx.studytracker.model.AssayTypeField;
import com.decibeltx.studytracker.model.AssayTypeField.AssayFieldType;
import com.decibeltx.studytracker.model.Collaborator;
import com.decibeltx.studytracker.model.Comment;
import com.decibeltx.studytracker.model.Conclusions;
import com.decibeltx.studytracker.model.ExternalLink;
import com.decibeltx.studytracker.model.Keyword;
import com.decibeltx.studytracker.model.NotebookEntryTemplate;
import com.decibeltx.studytracker.model.Program;
import com.decibeltx.studytracker.model.Status;
import com.decibeltx.studytracker.model.Study;
import com.decibeltx.studytracker.model.Task;
import com.decibeltx.studytracker.model.Task.TaskStatus;
import com.decibeltx.studytracker.model.User;
import com.decibeltx.studytracker.repository.ActivityRepository;
import com.decibeltx.studytracker.repository.AssayRepository;
import com.decibeltx.studytracker.repository.AssayTypeRepository;
import com.decibeltx.studytracker.repository.CollaboratorRepository;
import com.decibeltx.studytracker.repository.EntryTemplateRepository;
import com.decibeltx.studytracker.repository.KeywordRepository;
import com.decibeltx.studytracker.repository.ProgramRepository;
import com.decibeltx.studytracker.repository.StudyRepository;
import com.decibeltx.studytracker.repository.UserRepository;
import com.decibeltx.studytracker.service.StudyCommentService;
import com.decibeltx.studytracker.service.StudyConclusionsService;
import com.decibeltx.studytracker.service.StudyExternalLinkService;
import com.decibeltx.studytracker.service.StudyService;
import com.decibeltx.studytracker.storage.StorageFolder;
import com.decibeltx.studytracker.storage.StudyStorageService;
import com.decibeltx.studytracker.storage.exception.StudyStorageNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ExampleDataGenerator {

  public static final int PROGRAM_COUNT = 5;

  public static final int USER_COUNT = 3;

  public static final int COLLABORATOR_COUNT = 4;

  public static final int KEYWORD_COUNT = 7;

  public static final int ASSAY_TYPE_COUNT = 2;

  public static final int ASSAY_COUNT = 2;

  public static final int ENTRY_TEMPLATE_COUNT = 2;

  private static final Logger LOGGER = LoggerFactory.getLogger(ExampleDataGenerator.class);

  @Autowired
  private ProgramRepository programRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private CollaboratorRepository collaboratorRepository;

  @Autowired
  private AssayRepository assayRepository;

  @Autowired
  private ActivityRepository activityRepository;

  @Autowired
  private StudyStorageService studyStorageService;

  @Autowired
  private StudyService studyService;

  @Autowired
  private StudyExternalLinkService externalLinkService;

  @Autowired
  private StudyCommentService commentService;

  @Autowired
  private StudyConclusionsService conclusionsService;

  @Autowired
  private AssayTypeRepository assayTypeRepository;

  @Autowired
  private KeywordRepository keywordRepository;

  @Autowired
  private EntryTemplateRepository entryTemplateRepository;

  public List<NotebookEntryTemplate> generateExampleEntryTemplates(List<User> users) {
    User user = users.get(0);
    List<NotebookEntryTemplate> templates = new ArrayList<>();
    createEntryTemplate(user, templates, "id1", "table1", new Date());
    createEntryTemplate(user, templates, "id2", "table2", new Date());
    return templates;
  }

  private void createEntryTemplate(User user, List<NotebookEntryTemplate> templates,
                                   String templateId, String name, Date timeStamp) {
    NotebookEntryTemplate notebookEntryTemplate = NotebookEntryTemplate.of(user, templateId, name, timeStamp);
    Activity activity = EntryTemplateActivityUtils
            .fromNewEntryTemplate(notebookEntryTemplate, user);
    activityRepository.insert(activity);
    templates.add(notebookEntryTemplate);
  }

  public List<Program> generateExamplePrograms(List<User> users) {
    User user = users.get(0);
    List<Program> programs = new ArrayList<>();

    Program program = new Program();
    program.setName("Clinical Program A");
    program.setCode("CPA");
    program.setActive(true);
    program.setCreatedBy(user);
    program.setLastModifiedBy(user);
    program.setCreatedAt(new Date());
    programs.add(program);

    program = new Program();
    program.setName("Preclinical Project B");
    program.setCode("PPB");
    program.setActive(true);
    program.setCreatedBy(user);
    program.setLastModifiedBy(user);
    program.setCreatedAt(new Date());
    programs.add(program);

    program = new Program();
    program.setName("Cancelled Program C");
    program.setCode("CPC");
    program.setActive(false);
    program.setCreatedBy(user);
    program.setLastModifiedBy(user);
    program.setCreatedAt(new Date());
    programs.add(program);

    program = new Program();
    program.setName("Target ID Project D");
    program.setCode("TID");
    program.setActive(true);
    program.setCreatedBy(user);
    program.setLastModifiedBy(user);
    program.setCreatedAt(new Date());
    programs.add(program);

    program = new Program();
    program.setName("Target ID Project E");
    program.setCode("TID");
    program.setActive(true);
    program.setCreatedBy(user);
    program.setLastModifiedBy(user);
    program.setCreatedAt(new Date());
    programs.add(program);

    return programs;
  }

  public void createProgramFolders() {
    try {
      for (Program program : programRepository.findAll()) {
        try {
          studyStorageService.getProgramFolder(program);
        } catch (StudyStorageNotFoundException ex) {
          studyStorageService.createProgramFolder(program);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<User> generateExampleUsers() {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    List<User> users = new ArrayList<>();

    User user = new User();
    user.setUsername("jsmith");
    user.setPassword(encoder.encode("password"));
    user.setDisplayName("Joe Smith");
    user.setEmail("jsmith@email.com");
    user.setTitle("Director");
    user.setAdmin(false);
    user.setDepartment("Biology");
    users.add(user);

    user = new User();
    user.setUsername("ajohnson");
    user.setPassword(encoder.encode("password"));
    user.setDisplayName("Ann Johnson");
    user.setEmail("ajohnson@email.com");
    user.setTitle("Sr. Scientist");
    user.setAdmin(false);
    user.setDepartment("Biology");
    users.add(user);

    user = new User();
    user.setUsername("rblack");
    user.setPassword(encoder.encode("password"));
    user.setDisplayName("Rob Black");
    user.setEmail("rblack@email.com");
    user.setTitle("IT Admin");
    user.setAdmin(true);
    user.setDepartment("IT");
    users.add(user);

    return users;

  }

  public List<Keyword> generateExampleKeywords() {

    List<Keyword> keywords = new ArrayList<>();
    keywords.add(new Keyword("MCF7", "Cell Line"));
    keywords.add(new Keyword("HELA", "Cell Line"));
    keywords.add(new Keyword("A375", "Cell Line"));
    keywords.add(new Keyword("AKT1", "Gene"));
    keywords.add(new Keyword("AKT2", "Gene"));
    keywords.add(new Keyword("AKT3", "Gene"));
    keywords.add(new Keyword("PTEN", "Gene"));
    return keywords;

  }

  public List<Collaborator> generateExampleCollaborators() {

    List<Collaborator> collaborators = new ArrayList<>();

    Collaborator collaborator = new Collaborator();
    collaborator.setActive(true);
    collaborator.setLabel("Partner Co - In Vivo");
    collaborator.setOrganizationName("Partner Co");
    collaborator.setOrganizationLocation("China");
    collaborator.setContactPersonName("Joe Person");
    collaborator.setContactEmail("jperson@partnerco.com");
    collaborator.setCode("PC");
    collaborators.add(collaborator);

    collaborator = new Collaborator();
    collaborator.setActive(true);
    collaborator.setLabel("Partner Co - Chemistry");
    collaborator.setOrganizationName("Partner Co");
    collaborator.setOrganizationLocation("China");
    collaborator.setContactPersonName("Alex Person");
    collaborator.setContactEmail("aperson@partnerco.com");
    collaborator.setCode("PC");
    collaborators.add(collaborator);

    collaborator = new Collaborator();
    collaborator.setActive(true);
    collaborator.setLabel("University of Somewhere");
    collaborator.setOrganizationName("University of Somewhere");
    collaborator.setOrganizationLocation("Cambridge, MA");
    collaborator.setContactPersonName("John Scientist");
    collaborator.setContactEmail("jscientist@uos.edu");
    collaborator.setCode("US");
    collaborators.add(collaborator);

    collaborator = new Collaborator();
    collaborator.setActive(false);
    collaborator.setLabel("Inactive CRO");
    collaborator.setOrganizationName("Inactive CRO");
    collaborator.setOrganizationLocation("Cambridge, MA");
    collaborator.setCode("IN");
    collaborators.add(collaborator);

    return collaborators;

  }

  public void generateExampleStudies() throws Exception {

    List<Keyword> keywords = new ArrayList<>();
    keywords.add(keywordRepository.findByKeywordAndCategory("AKT1", "Gene")
        .orElseThrow(RecordNotFoundException::new));
    keywords.add(keywordRepository.findByKeywordAndCategory("MCF7", "Cell Line")
        .orElseThrow(RecordNotFoundException::new));

    // Study 1
    Program program = programRepository.findByName("Clinical Program A")
        .orElseThrow(RecordNotFoundException::new);
    User user = userRepository.findByUsername("jsmith")
        .orElseThrow(RecordNotFoundException::new);
    Collaborator collaborator = collaboratorRepository.findByLabel("University of Somewhere")
        .orElseThrow(RecordNotFoundException::new);
    Study study = new Study();
    study.setStatus(Status.IN_PLANNING);
    study.setName("Example Collaborator Study");
    //study.setCode(program.getCode() + "-10001");
    study.setProgram(program);
    study.setDescription(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    study.setLegacy(false);
    study.setActive(true);
    study.setCreatedBy(user);
    study.setLastModifiedBy(user);
    study.setStartDate(new Date());
    study.setOwner(user);
    study.setUsers(Collections.singletonList(user));
    study.setCollaborator(collaborator);
    study.setExternalCode(collaborator.getCode() + "-00001");
    study.setKeywords(keywords);

    NotebookFolder notebookEntry = new NotebookFolder();
    notebookEntry.setName("IDBS ELN");
    notebookEntry.setUrl(
        "https://decibel.idbs-eworkbook.com:8443/EWorkbookWebApp/#entity/displayEntity?entityId=603e68c0e01411e7acd000000a0000a2&v=y");
    notebookEntry.setReferenceId("12345");
    study.setNotebookFolder(notebookEntry);

    studyService.create(study);

    activityRepository.insert(StudyActivityUtils.fromNewStudy(study, user));

    ExternalLink link = new ExternalLink();
    link.setId(UUID.randomUUID().toString());
    link.setLabel("Google");
    link.setUrl(new URL("https://google.com"));
    externalLinkService.addStudyExternalLink(study, link);

    activityRepository.insert(StudyActivityUtils.fromNewExternalLink(study, user, link));

    // Study 2
    program = programRepository.findByName("Preclinical Project B")
        .orElseThrow(RecordNotFoundException::new);
    user = userRepository.findByUsername("ajohnson").orElseThrow(RecordNotFoundException::new);
    study = new Study();
    study.setStatus(Status.IN_PLANNING);
    study.setName("Example Study");
    //study.setCode(program.getCode() + "-10001");
    study.setProgram(program);
    study.setDescription(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    study.setLegacy(false);
    study.setActive(true);
    study.setCreatedBy(user);
    study.setLastModifiedBy(user);
    study.setStartDate(new Date());
    study.setOwner(user);
    study.setUsers(Collections.singletonList(user));
    study.setKeywords(keywords);

    notebookEntry = new NotebookFolder();
    notebookEntry.setName("ELN");
    notebookEntry.setUrl("https://google.com");
    notebookEntry.setReferenceId("12345");
    study.setNotebookFolder(notebookEntry);

    studyService.create(study);

    activityRepository.insert(StudyActivityUtils.fromNewStudy(study, user));

    studyService.updateStatus(study, Status.ACTIVE);

    activityRepository.insert(
        StudyActivityUtils.fromStudyStatusChange(study, user, Status.IN_PLANNING, Status.ACTIVE));

    Comment comment = new Comment();
    comment.setId(UUID.randomUUID().toString());
    comment.setCreatedAt(new Date());
    comment.setCreatedBy(user);
    comment.setText(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
    commentService.addStudyComment(study, comment);

    activityRepository.insert(StudyActivityUtils.fromNewComment(study, user, comment));

    Conclusions conclusions = new Conclusions();
    conclusions.setId(UUID.randomUUID().toString());
    conclusions.setCreatedAt(new Date());
    conclusions.setCreatedBy(user);
    conclusions.setContent(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
    conclusionsService.addStudyConclusions(study, conclusions);

    activityRepository.insert(StudyActivityUtils.fromNewConclusions(study, user, conclusions));

    // Study 3
    program = programRepository.findByName("Preclinical Project B")
        .orElseThrow(RecordNotFoundException::new);
    user = userRepository.findByUsername("ajohnson").orElseThrow(RecordNotFoundException::new);
    study = new Study();
    study.setStatus(Status.IN_PLANNING);
    study.setName("Example Legacy Study");
    study.setCode(program.getCode() + "-00001");
    study.setProgram(program);
    study.setDescription(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    study.setLegacy(true);
    study.setActive(true);
    study.setCreatedBy(user);
    study.setLastModifiedBy(user);
    study.setStartDate(new Date());
    study.setEndDate(new Date());
    study.setOwner(user);
    study.setUsers(Collections.singletonList(user));
    study.setKeywords(keywords);
    notebookEntry = new NotebookFolder();
    notebookEntry.setName("ELN");
    notebookEntry.setUrl(
        "https://google.com");
    study.setNotebookFolder(notebookEntry);
    studyService.create(study);

    activityRepository.insert(StudyActivityUtils.fromNewStudy(study, user));

    studyService.updateStatus(study, Status.COMPLETE);

    activityRepository.insert(
        StudyActivityUtils.fromStudyStatusChange(study, user, Status.IN_PLANNING, Status.COMPLETE));

    // Study 4
    program = programRepository.findByName("Clinical Program A")
        .orElseThrow(RecordNotFoundException::new);
    user = userRepository.findByUsername("jsmith").orElseThrow(RecordNotFoundException::new);
    study = new Study();
    study.setStatus(Status.IN_PLANNING);
    study.setName("Example Inactive Study");
    //study.setCode(program.getCode() + "-10002");
    study.setProgram(program);
    study.setDescription(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    study.setLegacy(false);
    study.setActive(false);
    study.setCreatedBy(user);
    study.setLastModifiedBy(user);
    study.setStartDate(new Date());
    study.setOwner(user);
    study.setUsers(Collections.singletonList(user));
    study.setKeywords(keywords);
    studyService.create(study);

    activityRepository.insert(StudyActivityUtils.fromNewStudy(study, user));

    studyService.updateStatus(study, Status.ON_HOLD);

    activityRepository.insert(
        StudyActivityUtils.fromStudyStatusChange(study, user, Status.IN_PLANNING, Status.ON_HOLD));

    // Study 5
    program = programRepository.findByName("Target ID Project D")
        .orElseThrow(RecordNotFoundException::new);
    user = userRepository.findByUsername("rblack").orElseThrow(RecordNotFoundException::new);
    study = new Study();
    study.setStatus(Status.IN_PLANNING);
    study.setName("Example Target ID Study 1");
    //study.setCode(program.getCode() + "-10001");
    study.setProgram(program);
    study.setDescription(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    study.setLegacy(false);
    study.setActive(true);
    study.setCreatedBy(user);
    study.setLastModifiedBy(user);
    study.setStartDate(new Date());
    study.setEndDate(new Date());
    study.setOwner(user);
    study.setUsers(Collections.singletonList(user));
    study.setKeywords(keywords);
    studyService.create(study);
    activityRepository.insert(StudyActivityUtils.fromNewStudy(study, user));
    studyService.updateStatus(study, Status.COMPLETE);
    activityRepository.insert(
        StudyActivityUtils.fromStudyStatusChange(study, user, Status.IN_PLANNING, Status.COMPLETE));

    // Study 6
    program = programRepository.findByName("Target ID Project E")
        .orElseThrow(RecordNotFoundException::new);
    user = userRepository.findByUsername("rblack").orElseThrow(RecordNotFoundException::new);
    study = new Study();
    study.setStatus(Status.IN_PLANNING);
    study.setName("Example Target ID Study 2");
    study.setCode(program.getCode() + "-10002");
    study.setProgram(program);
    study.setDescription(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    study.setLegacy(false);
    study.setActive(true);
    study.setCreatedBy(user);
    study.setLastModifiedBy(user);
    study.setStartDate(new Date());
    study.setEndDate(new Date());
    study.setOwner(user);
    study.setUsers(Collections.singletonList(user));
    study.setKeywords(keywords);
    studyService.create(study);
    activityRepository.insert(StudyActivityUtils.fromNewStudy(study, user));

  }

  public void createStudyFolders() {
    for (Study study : studyRepository.findAll()) {
      try {
        StorageFolder folder;
        try {
          folder = studyStorageService.getStudyFolder(study);
        } catch (Exception e) {
          folder = studyStorageService.createStudyFolder(study);
        }
        study.setStorageFolder(folder);
        study.setUpdatedAt(new Date());
        studyRepository.save(study);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  public List<AssayType> generateExampleAssayTypes() {

    List<AssayType> assayTypes = new ArrayList<>();

    AssayType assayType = new AssayType();
    assayType.setName("Generic");
    assayType.setDescription("Generic assay type for all purposes");
    assayType.setActive(true);
    assayTypes.add(assayType);

    assayType = new AssayType();
    assayType.setName("Histology");
    assayType.setDescription("Histological analysis assays");
    assayType.setActive(true);
    assayType.setFields(Arrays.asList(
        new AssayTypeField("No. Slides", "number_of_slides", AssayFieldType.INTEGER, true),
        new AssayTypeField("Antibodies", "antibodies", AssayFieldType.TEXT),
        new AssayTypeField("Concentration (ul/mg)", "concentration", AssayFieldType.FLOAT),
        new AssayTypeField("Date", "date", AssayFieldType.DATE),
        new AssayTypeField("External", "external", AssayFieldType.BOOLEAN, true),
        new AssayTypeField("Stain", "stain", AssayFieldType.STRING)
    ));
    assayType.setTasks(Arrays.asList(
        new Task("Embed tissue", TaskStatus.TODO, 0),
        new Task("Cut slides", TaskStatus.TODO, 1),
        new Task("Stain slides", TaskStatus.TODO, 2)
    ));
    assayTypes.add(assayType);

    return assayTypes;

  }

  public List<Assay> generateExampleAssays(List<Study> studies) {

    List<Assay> assays = new ArrayList<>();
    AssayType assayType = assayTypeRepository.findByName("Generic")
        .orElseThrow(RecordNotFoundException::new);

    Study study = studies.stream()
        .filter(s -> s.getCode().equals("PPB-10001"))
        .collect(Collectors.toList())
        .get(0);
    User user = study.getOwner();
    Assay assay = new Assay();
    assay.setStudy(study);
    assay.setActive(true);
    assay.setCode(study.getCode() + "-001");
    assay.setName("Histology assay");
    assay.setDescription(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ");
    assay.setStatus(Status.ACTIVE);
    assay.setStartDate(new Date());
    assay.setAssayType(assayType);
    assay.setOwner(user);
    assay.setCreatedBy(user);
    assay.setUsers(Collections.singletonList(user));
    assay.setLastModifiedBy(user);
    assay.setUpdatedAt(new Date());
    assay.setAttributes(Collections.singletonMap("key", "value"));
    assay.setTasks(Collections.singletonList(new Task("My task")));
    assays.add(assay);

    assay = new Assay();
    assay.setStudy(study);
    assay.setActive(true);
    assay.setCode(study.getCode() + "-00002");
    assay.setName("In vivo assay");
    assay.setDescription(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. ");
    assay.setStatus(Status.COMPLETE);
    assay.setStartDate(new Date());
    assay.setEndDate(new Date());
    assay.setAssayType(assayType);
    assay.setOwner(user);
    assay.setCreatedBy(user);
    assay.setUsers(Collections.singletonList(user));
    assay.setLastModifiedBy(user);
    assay.setUpdatedAt(new Date());
    assay.setAttributes(Collections.singletonMap("key", "value"));
    assay.setTasks(Collections.singletonList(new Task("My task")));
    assays.add(assay);

    return assays;

  }

  public void createAssayFolders() {
    for (Assay assay : assayRepository.findAll()) {
      try {
        StorageFolder folder;
        try {
          folder = studyStorageService.getAssayFolder(assay);
        } catch (Exception e) {
          folder = studyStorageService.createAssayFolder(assay);
        }
        assay.setStorageFolder(folder);
        assayRepository.save(assay);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  public void clearDatabase() {
    LOGGER.info("Wiping collections...");
    entryTemplateRepository.deleteAll();
    programRepository.deleteAll();
    userRepository.deleteAll();
    collaboratorRepository.deleteAll();
    keywordRepository.deleteAll();
    studyRepository.deleteAll();
    assayRepository.deleteAll();
    activityRepository.deleteAll();
    assayTypeRepository.deleteAll();
  }

  public void populateDatabase() {
    try {

      LOGGER.info("Preparing to populate database with example data...");
      this.clearDatabase();
      LOGGER.info("Inserting example data...");
      userRepository.insert(generateExampleUsers());
      programRepository.insert(generateExamplePrograms(userRepository.findAll()));
      assayTypeRepository.insert(generateExampleAssayTypes());
      createProgramFolders();
      keywordRepository.insert(generateExampleKeywords());
      collaboratorRepository.insert(generateExampleCollaborators());
      generateExampleStudies();
      entryTemplateRepository.insert(generateExampleEntryTemplates(userRepository.findAll()));

      for (Assay assay : generateExampleAssays(studyRepository.findAll())) {
        assayRepository.insert(assay);
        Study study = assay.getStudy();
        study.getAssays().add(assay);
        studyRepository.save(study);
      }
      createAssayFolders();

      LOGGER.info("Done.");

    } catch (Exception e) {
      throw new StudyTrackerException(e);
    }
  }

}
