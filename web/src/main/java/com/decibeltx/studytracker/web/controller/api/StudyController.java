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

package com.decibeltx.studytracker.web.controller.api;

import com.decibeltx.studytracker.core.events.StudyEventPublisher;
import com.decibeltx.studytracker.core.exception.RecordNotFoundException;
import com.decibeltx.studytracker.core.model.Assay;
import com.decibeltx.studytracker.core.model.Study;
import com.decibeltx.studytracker.core.service.AssayService;
import com.decibeltx.studytracker.core.service.ProgramService;
import com.decibeltx.studytracker.core.service.StudyService;
import com.decibeltx.studytracker.core.service.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class StudyController {

  private StudyService studyService;
  private UserService userService;
  private ProgramService programService;
  private AssayService assayService;
  private StudyEventPublisher studyEventPublisher;

  protected Study getStudyFromIdentifier(String id) {
    Study study;
    Optional<Study> optional = studyService.findById(id);
    if (optional.isPresent()) {
      study = optional.get();
    } else {
      optional = studyService.findByCode(id);
      if (optional.isPresent()) {
        study = optional.get();
      } else {
        throw new RecordNotFoundException();
      }
    }
    return study;
  }

  protected Assay getAssayFromIdentifier(String id) {
    Assay assay;
    Optional<Assay> optional = assayService.findById(id);
    if (optional.isPresent()) {
      assay = optional.get();
    } else {
      optional = assayService.findByCode(id);
      if (optional.isPresent()) {
        assay = optional.get();
      } else {
        throw new RecordNotFoundException();
      }
    }
    return assay;
  }

  public StudyService getStudyService() {
    return studyService;
  }

  @Autowired
  public void setStudyService(StudyService studyService) {
    this.studyService = studyService;
  }

  public UserService getUserService() {
    return userService;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public ProgramService getProgramService() {
    return programService;
  }

  @Autowired
  public void setProgramService(ProgramService programService) {
    this.programService = programService;
  }

  public AssayService getAssayService() {
    return assayService;
  }

  @Autowired
  public void setAssayService(AssayService assayService) {
    this.assayService = assayService;
  }

  public StudyEventPublisher getStudyEventPublisher() {
    return studyEventPublisher;
  }

  @Autowired
  public void setStudyEventPublisher(
      StudyEventPublisher studyEventPublisher) {
    this.studyEventPublisher = studyEventPublisher;
  }
}
