package com.decibeltx.studytracker.controller.api;

import com.decibeltx.studytracker.mapstruct.dto.StudyCollectionDto;
import com.decibeltx.studytracker.mapstruct.mapper.StudyCollectionMapper;
import com.decibeltx.studytracker.model.Study;
import com.decibeltx.studytracker.model.StudyCollection;
import com.decibeltx.studytracker.service.StudyCollectionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/study/{studyId}/studycollection")
public class StudyStudyCollectionController extends AbstractStudyController {

  @Autowired
  private StudyCollectionService studyCollectionService;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private StudyCollectionMapper mapper;

  @GetMapping("")
  public List<StudyCollectionDto> getStudyStudyCollections(@PathVariable("studyId") String studyId) {
    Study study = this.getStudyFromIdentifier(studyId);
    List<StudyCollection> collections = studyCollectionService.findByStudy(study);
    return mapper.toDtoList(collections);
  }

}
