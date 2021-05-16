package com.decibeltx.studytracker.controller.api;

import com.decibeltx.studytracker.eln.NotebookFolder;
import com.decibeltx.studytracker.eln.StudyNotebookService;
import com.decibeltx.studytracker.exception.RecordNotFoundException;
import com.decibeltx.studytracker.model.Assay;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/assay/{assayId}/notebook")
@RestController
public class AssayNotebookController extends AbstractAssayController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AssayNotebookController.class);

  @Autowired(required = false)
  private StudyNotebookService studyNotebookService;

  @GetMapping("")
  public NotebookFolder getNotebookFolder(@PathVariable("assayId") Long assayId)
          throws RecordNotFoundException {
    LOGGER.info("Fetching notebook folder for assay: " + assayId);
    Assay assay = getAssayFromIdentifier(assayId);

    Optional<NotebookFolder> notebookFolder = Optional.ofNullable(studyNotebookService)
            .flatMap(service -> service.findAssayFolder(assay));
    return notebookFolder
            .orElseThrow(() -> new RecordNotFoundException("Could not load assay folder"));
  }
}
