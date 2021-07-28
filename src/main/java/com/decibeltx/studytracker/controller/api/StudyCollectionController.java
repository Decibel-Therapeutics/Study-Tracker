package com.decibeltx.studytracker.controller.api;

import com.decibeltx.studytracker.controller.UserAuthenticationUtils;
import com.decibeltx.studytracker.exception.InsufficientPrivilegesException;
import com.decibeltx.studytracker.exception.InvalidConstraintException;
import com.decibeltx.studytracker.exception.RecordNotFoundException;
import com.decibeltx.studytracker.mapstruct.dto.StudyCollectionDto;
import com.decibeltx.studytracker.mapstruct.mapper.StudyCollectionMapper;
import com.decibeltx.studytracker.model.Study;
import com.decibeltx.studytracker.model.StudyCollection;
import com.decibeltx.studytracker.model.User;
import com.decibeltx.studytracker.service.StudyCollectionService;
import com.decibeltx.studytracker.service.StudyService;
import com.decibeltx.studytracker.service.UserService;
import java.util.List;
import javax.validation.Valid;
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

@RestController
@RequestMapping("/api/studycollection")
public class StudyCollectionController {

  private static final Logger LOGGER = LoggerFactory.getLogger(StudyCollectionController.class);

  @Autowired
  private StudyCollectionService studyCollectionService;

  @Autowired
  private UserService userService;

  @Autowired
  private StudyService studyService;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private StudyCollectionMapper mapper;

  @GetMapping("")
  public List<StudyCollectionDto> getStudyCollections() {
    return mapper.toDtoList(studyCollectionService.findAll());
  }

  @PostMapping("")
  public HttpEntity<StudyCollectionDto> createCollection(@RequestBody @Valid StudyCollectionDto payload) {

    String username = UserAuthenticationUtils
        .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
    User currentUser = userService.findByUsername(username)
        .orElseThrow(RecordNotFoundException::new);

    StudyCollection collection = mapper.fromDto(payload);

    // Make sure a collection owned by the same user does not exist already
    if (studyCollectionService.collectionWithNameExists(collection.getName(), currentUser)) {
      throw new InvalidConstraintException("A study collection with this name already exists.");
    }

    StudyCollectionDto output = mapper.toDto(studyCollectionService.create(collection));
    return new ResponseEntity<>(output, HttpStatus.CREATED);

  }

  @PutMapping("/{id}")
  public HttpEntity<StudyCollectionDto> updateCollection(@PathVariable("id") Long id,
      @RequestBody @Valid StudyCollectionDto dto) {

    studyCollectionService.findById(id)
        .orElseThrow(() -> new RecordNotFoundException("Study collection not found: " + id));
    StudyCollection collection = mapper.fromDto(dto);

    String username = UserAuthenticationUtils
        .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
    User currentUser = userService.findByUsername(username)
        .orElseThrow(RecordNotFoundException::new);

    // If collections is not public, only owner can edit
    if (!dto.isShared() && !currentUser.getId().equals(collection.getCreatedBy().getId())) {
      throw new InsufficientPrivilegesException("You do not have permission to modify this study collection.");
    }

    // Make sure a collection owned by the same user does not exist already
    if (studyCollectionService.collectionWithNameExists(collection.getName(), currentUser)) {
      throw new InvalidConstraintException("A study collection with this name already exists.");
    }

    studyCollectionService.update(collection);

    StudyCollection output = studyCollectionService.findById(id)
        .orElseThrow(() -> new RecordNotFoundException("Study collection not found: " + id));
    return new ResponseEntity<>(mapper.toDto(output), HttpStatus.OK);

  }

  @DeleteMapping("/{id}")
  public HttpEntity<?> deleteCollection(@PathVariable("id") Long id) {

    StudyCollection collection = studyCollectionService.findById(id)
        .orElseThrow(() -> new RecordNotFoundException("Study collection not found: " + id));

    String username = UserAuthenticationUtils
        .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
    User currentUser = userService.findByUsername(username)
        .orElseThrow(RecordNotFoundException::new);

    if (!currentUser.getId().equals(collection.getCreatedBy().getId())) {
      throw new InsufficientPrivilegesException("Only the study collection owner may delete it.");
    }

    studyCollectionService.delete(collection);

    return new ResponseEntity<>(HttpStatus.OK);

  }

  @PostMapping("/{collectionId}/{studyId}")
  public HttpEntity<?> addStudyToCollection(@PathVariable("collectionId") Long collectionId,
      @PathVariable("studyId") Long studyId) {

    StudyCollection collection = studyCollectionService.findById(collectionId)
        .orElseThrow(() -> new RecordNotFoundException("Study collection not found: " + collectionId));
    Study study = studyService.findById(studyId)
        .orElseThrow(() -> new RecordNotFoundException("Study does not exist: " + studyId));

    collection.addStudy(study);
    studyCollectionService.update(collection);

    return new ResponseEntity<>(HttpStatus.OK);

  }

  @DeleteMapping("/{collectionId}/{studyId}")
  public HttpEntity<?> removeStudyFromCollection(@PathVariable("collectionId") Long collectionId,
      @PathVariable("studyId") Long studyId) {

    StudyCollection collection = studyCollectionService.findById(collectionId)
        .orElseThrow(() -> new RecordNotFoundException("Study collection not found: " + collectionId));
    Study study = studyService.findById(studyId)
        .orElseThrow(() -> new RecordNotFoundException("Study does not exist: " + studyId));

    collection.removeStudy(study);
    studyCollectionService.update(collection);

    return new ResponseEntity<>(HttpStatus.OK);

  }

}
