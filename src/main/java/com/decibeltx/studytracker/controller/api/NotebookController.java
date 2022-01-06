package com.decibeltx.studytracker.controller.api;

import com.decibeltx.studytracker.eln.NotebookEntryTemplate;
import com.decibeltx.studytracker.eln.StudyNotebookService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/eln")
public class NotebookController {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotebookController.class);

  @Autowired
  private StudyNotebookService notebookService;

  @GetMapping("/entrytemplate")
  public List<NotebookEntryTemplate> findNotebookEntryTemplates() {
    List<NotebookEntryTemplate> templates = notebookService.findEntryTemplates();
    LOGGER.info(templates.toString());
    return templates;
  }

}
