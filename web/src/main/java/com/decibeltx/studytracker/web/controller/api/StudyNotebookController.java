package com.decibeltx.studytracker.web.controller.api;

import com.decibeltx.studytracker.core.eln.NotebookFolder;
import com.decibeltx.studytracker.core.eln.StudyNotebookService;
import com.decibeltx.studytracker.core.exception.RecordNotFoundException;
import com.decibeltx.studytracker.core.model.Study;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/study/{studyId}/notebook")
public class StudyNotebookController extends AbstractStudyController {

  private static final Logger LOGGER = LoggerFactory.getLogger(StudyStorageController.class);

  @Autowired(required = false)
  private StudyNotebookService studyNotebookService;

  @GetMapping("")
  public NotebookFolder getStudyNotebookFolder(@PathVariable("studyId") String studyId)
          throws RecordNotFoundException {
    LOGGER.info("Fetching notebook folder for study: " + studyId);
    Study study = getStudyFromIdentifier(studyId);

    Optional<NotebookFolder> notebookFolder = (studyNotebookService == null) ?
            Optional.empty() : studyNotebookService.findStudyFolder(study);

    if (notebookFolder.isEmpty())
      throw new RecordNotFoundException("Could not load NoteBook folder");

    return notebookFolder.get();
  }
}