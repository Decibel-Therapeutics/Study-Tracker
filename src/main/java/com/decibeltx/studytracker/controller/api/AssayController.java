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

package com.decibeltx.studytracker.controller.api;

import com.decibeltx.studytracker.controller.UserAuthenticationUtils;
import com.decibeltx.studytracker.exception.RecordNotFoundException;
import com.decibeltx.studytracker.exception.StudyTrackerException;
import com.decibeltx.studytracker.mapstruct.dto.ActivitySummaryDto;
import com.decibeltx.studytracker.mapstruct.dto.AssayDetailsDto;
import com.decibeltx.studytracker.mapstruct.dto.AssayFormDto;
import com.decibeltx.studytracker.mapstruct.dto.AssayParentDto;
import com.decibeltx.studytracker.mapstruct.mapper.ActivityMapper;
import com.decibeltx.studytracker.mapstruct.mapper.AssayMapper;
import com.decibeltx.studytracker.model.Assay;
import com.decibeltx.studytracker.model.Status;
import com.decibeltx.studytracker.model.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assay")
public class AssayController extends AbstractAssayController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AssayController.class);

  @Autowired
  private AssayMapper assayMapper;

  @Autowired
  private ActivityMapper activityMapper;

  @GetMapping("")
  public List<AssayParentDto> findAll() {
    return assayMapper.toAssayParentList(getAssayService().findAll().stream()
        .filter(Assay::isActive)
        .filter(a -> a.getStudy().isActive())
        .collect(Collectors.toList()));
  }

  @GetMapping("/{id}")
  public AssayDetailsDto findById(@PathVariable("id") String assayId) throws RecordNotFoundException {
    return assayMapper.toAssayDetails(getAssayFromIdentifier(assayId));
  }

  @PutMapping("/{id}")
  public HttpEntity<AssayDetailsDto> update(@PathVariable("id") Long id,
      @RequestBody @Valid AssayFormDto dto) {

    LOGGER.info("Updating assay with id: " + id);
    LOGGER.info(dto.toString());

    Assay assay = assayMapper.fromAssayForm(dto);

    // Get authenticated user
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = UserAuthenticationUtils.getUsernameFromAuthentication(authentication);
    User user = getUserService().findByUsername(username)
        .orElseThrow(RecordNotFoundException::new);

    Assay updated = updateAssay(assay, user);

    return new ResponseEntity<>(assayMapper.toAssayDetails(updated), HttpStatus.CREATED);

  }

  @DeleteMapping("/{id}")
  public HttpEntity<?> delete(@PathVariable("id") String id) {

    LOGGER.info("Deleting assay: " + id);

    // Get authenticated user
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = UserAuthenticationUtils.getUsernameFromAuthentication(authentication);
    User user = this.getUserService().findByUsername(username)
        .orElseThrow(RecordNotFoundException::new);

    this.deleteAssay(id, user);

    return new ResponseEntity<>(HttpStatus.OK);

  }

  @PostMapping("/{id}/status")
  public HttpEntity<?> updateStatus(@PathVariable("id") String id,
      @RequestBody Map<String, Object> params) throws StudyTrackerException {

    if (!params.containsKey("status")) {
      throw new StudyTrackerException("No status label provided.");
    }

    Assay assay = this.getAssayFromIdentifier(id);

    // Get authenticated user
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = UserAuthenticationUtils.getUsernameFromAuthentication(authentication);
    User user = this.getUserService().findByUsername(username)
        .orElseThrow(RecordNotFoundException::new);

    String label = (String) params.get("status");
    Status status = Status.valueOf(label);
    LOGGER.info(String.format("Setting status of assay %s to %s", id, label));

    this.updateAssayStatus(assay.getId(), status, user);

    return new ResponseEntity<>(HttpStatus.OK);

  }

  @GetMapping("/{assayId}/activity")
  public List<ActivitySummaryDto> getAssayActivity(@PathVariable("assayId") String assayId) {
    Assay assay = this.getAssayFromIdentifier(assayId);
    return activityMapper.toActivitySummaryList(getActivityService().findByAssay(assay));
  }

}
