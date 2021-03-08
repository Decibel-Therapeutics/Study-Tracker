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

import com.decibeltx.studytracker.core.events.util.StudyActivityUtils;
import com.decibeltx.studytracker.core.exception.RecordNotFoundException;
import com.decibeltx.studytracker.core.model.Activity;
import com.decibeltx.studytracker.core.model.ExternalLink;
import com.decibeltx.studytracker.core.model.Study;
import com.decibeltx.studytracker.core.model.User;
import com.decibeltx.studytracker.core.service.StudyExternalLinkService;
import com.decibeltx.studytracker.web.controller.UserAuthenticationUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@RequestMapping("/api/study/{id}/links")
@RestController
public class StudyExternalLinksController extends AbstractStudyController {

  @Autowired
  private StudyExternalLinkService studyExternalLinkService;

  @GetMapping("")
  @ApiOperation(
      value = "",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ApiResponses({
      @ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 404, message = "Not found")
  })
  public List<ExternalLink> getStudyExternalLinks(@PathVariable("id") String studyId) {
    Study study = getStudyFromIdentifier(studyId);
    return study.getExternalLinks();
  }

  @PostMapping("")
  @ApiOperation(
      value = "",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ApiResponses({
      @ApiResponse(code = 201, message = "Created"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 401, message = "Unauthorized"),
      @ApiResponse(code = 404, message = "Not found")
  })
  public HttpEntity<?> addExternalLink(@PathVariable("id") String studyId,
      @RequestBody ExternalLink externalLink) {
    Study study = getStudyFromIdentifier(studyId);
    String username = UserAuthenticationUtils
        .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
    User user = getUserService().findByUsername(username)
        .orElseThrow(RecordNotFoundException::new);
    study.setLastModifiedBy(user);
    studyExternalLinkService.addStudyExternalLink(study, externalLink);

    // Publish events
    Activity activity = StudyActivityUtils.fromNewExternalLink(study, user, externalLink);
    getActivityService().create(activity);
    getEventsService().dispatchEvent(activity);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/{linkId}")
  @ApiOperation(
      value = "",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ApiResponses({
      @ApiResponse(code = 201, message = "Created"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 401, message = "Unauthorized"),
      @ApiResponse(code = 404, message = "Not found")
  })
  public HttpEntity<?> editExternalLink(@PathVariable("id") String studyId,
      @PathVariable("linkId") String linkId, @RequestBody ExternalLink externalLink) {
    Study study = getStudyFromIdentifier(studyId);
    String username = UserAuthenticationUtils
        .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
    User user = getUserService().findByUsername(username)
        .orElseThrow(RecordNotFoundException::new);
    study.setLastModifiedBy(user);
    Optional<ExternalLink> optional = studyExternalLinkService
        .findStudyExternalLinkById(study, linkId);
    if (!optional.isPresent()) {
      throw new RecordNotFoundException("Cannot find external link with ID: " + linkId);
    }
    studyExternalLinkService.updateStudyExternalLink(study, externalLink);

    // Publish events
    Activity activity = StudyActivityUtils.fromUpdatedExternalLink(study, user, externalLink);
    getActivityService().create(activity);
    getEventsService().dispatchEvent(activity);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @DeleteMapping("/{linkId}")
  @ApiOperation(
      value = "",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ApiResponses({
      @ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 401, message = "Unauthorized"),
      @ApiResponse(code = 404, message = "Not found")
  })
  public HttpEntity<?> removeExternalLink(@PathVariable("id") String studyId,
      @PathVariable("linkId") String linkId) {
    Study study = getStudyFromIdentifier(studyId);
    String username = UserAuthenticationUtils
        .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
    User user = getUserService().findByUsername(username)
        .orElseThrow(RecordNotFoundException::new);
    study.setLastModifiedBy(user);
    studyExternalLinkService.deleteStudyExternalLink(study, linkId);

    // Publish events
    Activity activity = StudyActivityUtils.fromDeletedExternalLink(study, user);
    getActivityService().create(activity);
    getEventsService().dispatchEvent(activity);

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
