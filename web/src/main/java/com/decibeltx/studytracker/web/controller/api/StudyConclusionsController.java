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

import com.decibeltx.studytracker.core.exception.RecordNotFoundException;
import com.decibeltx.studytracker.core.exception.StudyTrackerException;
import com.decibeltx.studytracker.core.model.Activity;
import com.decibeltx.studytracker.core.model.Conclusions;
import com.decibeltx.studytracker.core.model.Study;
import com.decibeltx.studytracker.core.model.User;
import com.decibeltx.studytracker.core.service.StudyConclusionsService;
import com.decibeltx.studytracker.core.service.util.StudyActivityUtils;
import com.decibeltx.studytracker.web.controller.UserAuthenticationUtils;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/study/{studyId}/conclusions")
@RestController
public class StudyConclusionsController extends StudyController {

  private static final Logger LOGGER = LoggerFactory.getLogger(StudyConclusionsController.class);

  @Autowired
  private StudyConclusionsService studyConclusionsService;

  @GetMapping("")
  public Conclusions getStudyConclusions(@PathVariable("studyId") String studyId) {
    Study study = getStudyFromIdentifier(studyId);
    Optional<Conclusions> optional = studyConclusionsService.findStudyConclusions(study);
    if (optional.isPresent()) {
      return optional.get();
    }
    throw new RecordNotFoundException("No conclusions found for study: " + study.getCode());
  }

  @PostMapping("")
  public HttpEntity<?> newStudyConclusions(@PathVariable("studyId") String studyId,
      @RequestBody Conclusions conclusions) {
    Study study = getStudyFromIdentifier(studyId);
    if (conclusions.getId() != null || study.getConclusions() != null) {
      throw new StudyTrackerException("Study conclusions object already exists.");
    }
    LOGGER.info(
        String.format("Creating conclusions for study %s: %s", studyId, conclusions.toString()));

    String username = UserAuthenticationUtils
        .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
    User user = getUserService().findByUsername(username)
        .orElseThrow(RecordNotFoundException::new);
    study.setLastModifiedBy(user);
    conclusions.setCreatedBy(user);
    studyConclusionsService.addStudyConclusions(study, conclusions);

    // Publish events
    Activity activity = StudyActivityUtils.fromNewConclusions(study, user, conclusions);
    getActivityService().create(activity);
    getEventsService().dispatchEvent(activity);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("")
  public HttpEntity<?> editStudyConclusions(@PathVariable("studyId") String studyId,
      @RequestBody Conclusions conclusions) {
    LOGGER.info(
        String.format("Updating conclusions for study %s: %s", studyId, conclusions.toString()));
    Study study = getStudyFromIdentifier(studyId);
    String username = UserAuthenticationUtils
        .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
    User user = getUserService().findByUsername(username)
        .orElseThrow(RecordNotFoundException::new);
    study.setLastModifiedBy(user);
    conclusions.setLastModifiedBy(user);
    studyConclusionsService.updateStudyConclusions(study, conclusions);

    Activity activity = StudyActivityUtils.fromUpdatedConclusions(study, user, conclusions);
    getActivityService().create(activity);
    getEventsService().dispatchEvent(activity);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("")
  public HttpEntity<?> deleteStudyConclusions(@PathVariable("studyId") String studyId) {
    LOGGER.info(String.format("Deleting conclusions for study %s", studyId));
    Study study = getStudyFromIdentifier(studyId);
    String username = UserAuthenticationUtils
        .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
    User user = getUserService().findByUsername(username)
        .orElseThrow(RecordNotFoundException::new);
    study.setLastModifiedBy(user);
    studyConclusionsService.deleteStudyConclusions(study);

    Activity activity = StudyActivityUtils.fromDeletedConclusions(study, user);
    getActivityService().create(activity);
    getEventsService().dispatchEvent(activity);

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
