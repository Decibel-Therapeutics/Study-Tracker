package com.decibeltx.studytracker.web.controller.api;

import com.decibeltx.studytracker.core.eln.NotebookEntry;
import com.decibeltx.studytracker.core.eln.StudyNotebookService;
import com.decibeltx.studytracker.core.exception.RecordNotFoundException;
import com.decibeltx.studytracker.core.model.Assay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/assay/{assayId}/addEntry")
public class AssayEntryController extends AbstractAssayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssayEntryController.class);

    @Autowired(required = false)
    private StudyNotebookService studyNotebookService;

    @GetMapping("")
    public NotebookEntry createEntry(@PathVariable("assayId") String assayId) throws RecordNotFoundException {
        System.out.println("ENTERED !!!");
        Assay assay = getAssayFromIdentifier(assayId);
        LOGGER.info("Adding entry for assay: " + assayId);
        return Optional.ofNullable(studyNotebookService)
                .map(service -> service.createNotebook(assay, "tmpl_LwR3TYWL")).
                        orElseThrow(() -> new RecordNotFoundException("Could not create new entry"));
    }
}
