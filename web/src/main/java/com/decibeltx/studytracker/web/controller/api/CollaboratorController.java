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
import com.decibeltx.studytracker.core.model.Collaborator;
import com.decibeltx.studytracker.core.service.CollaboratorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RestController
@RequestMapping("/api/collaborator")
public class CollaboratorController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CollaboratorController.class);

  @Autowired
  private CollaboratorService collaboratorService;

  @GetMapping("")
  @ApiOperation(
      value = "",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ApiResponses({
      @ApiResponse(code = 200, message = "Ok")
  })
  public List<Collaborator> getAllExternalContacts(
      @RequestParam(value = "label", required = false) String label,
      @RequestParam(value = "organizationName", required = false) String name,
      @RequestParam(value = "organizationCode", required = false) String code
  ) {
    if (label != null) {
      Optional<Collaborator> optional = collaboratorService.findByLabel(label);
      return optional.isPresent() ? Collections.singletonList(optional.get()) : new ArrayList<>();
    } else if (name != null) {
      return collaboratorService.findByOrganizationName(name);
    } else if (code != null) {
      return collaboratorService.findByCode(code);
    }
    return collaboratorService.findAll();
  }

  @GetMapping("/{id}")
  @ApiOperation(
      value = "",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ApiResponses({
      @ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 404, message = "Not found")
  })
  public Collaborator getExternalContact(@PathVariable("id") String id) throws Exception {
    Optional<Collaborator> optional = collaboratorService.findById(id);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      throw new RecordNotFoundException();
    }
  }

  @PostMapping("")
  @ApiOperation(
      value = "",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ApiResponses({
      @ApiResponse(code = 200, message = "Ok"),
      @ApiResponse(code = 400, message = "Bad request"),
      @ApiResponse(code = 401, message = "Unauthorized")
  })
  public Collaborator createNewCollaborator(@Valid @RequestBody Collaborator collaborator) {
    LOGGER.info("Creating new collaborator record");
    collaboratorService.create(collaborator);
    LOGGER.info(collaborator.toString());
    return collaborator;
  }

}
