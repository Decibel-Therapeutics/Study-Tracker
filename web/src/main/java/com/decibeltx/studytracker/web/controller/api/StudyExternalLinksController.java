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
import com.decibeltx.studytracker.core.model.ExternalLink;
import com.decibeltx.studytracker.core.model.Study;
import com.decibeltx.studytracker.core.model.User;
import com.decibeltx.studytracker.core.service.StudyExternalLinkService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class StudyExternalLinksController extends StudyController {

  @Autowired
  private StudyExternalLinkService studyExternalLinkService;

  @GetMapping("")
  public List<ExternalLink> getStudyExternalLinks(@PathVariable("id") String studyId) {
    Study study = getStudyFromIdentifier(studyId);
    return study.getExternalLinks();
  }

  @PostMapping("")
  public HttpEntity<?> addExternalLink(@PathVariable("id") String studyId,
      @RequestBody ExternalLink externalLink) {
    Study study = getStudyFromIdentifier(studyId);
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    User user = getUserService().findByAccountName(userDetails.getUsername())
        .orElseThrow(RecordNotFoundException::new);
    study.setLastModifiedBy(user);
    studyExternalLinkService.addStudyExternalLink(study, externalLink);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/{linkId}")
  public HttpEntity<?> editExternalLink(@PathVariable("id") String studyId,
      @PathVariable("linkId") String linkId, @RequestBody ExternalLink externalLink) {
    Study study = getStudyFromIdentifier(studyId);
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    User user = getUserService().findByAccountName(userDetails.getUsername())
        .orElseThrow(RecordNotFoundException::new);
    study.setLastModifiedBy(user);
    Optional<ExternalLink> optional = studyExternalLinkService
        .findStudyExternalLinkById(study, linkId);
    if (!optional.isPresent()) {
      throw new RecordNotFoundException("Cannot find external link with ID: " + linkId);
    }
    studyExternalLinkService.updateStudyExternalLink(study, externalLink);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @DeleteMapping("/{linkId}")
  public HttpEntity<?> removeExternalLink(@PathVariable("id") String studyId,
      @PathVariable("linkId") String linkId) {
    Study study = getStudyFromIdentifier(studyId);
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    User user = getUserService().findByAccountName(userDetails.getUsername())
        .orElseThrow(RecordNotFoundException::new);
    study.setLastModifiedBy(user);
    studyExternalLinkService.deleteStudyExternalLink(study, linkId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
