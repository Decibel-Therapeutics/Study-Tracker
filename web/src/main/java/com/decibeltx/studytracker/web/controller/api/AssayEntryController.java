package com.decibeltx.studytracker.web.controller.api;

import com.decibeltx.studytracker.benchling.eln.entities.BenchlingEntryTemplate;
import com.decibeltx.studytracker.core.eln.NotebookEntry;
import com.decibeltx.studytracker.core.eln.StudyNotebookService;
import com.decibeltx.studytracker.core.exception.RecordNotFoundException;
import com.decibeltx.studytracker.core.model.Assay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assayEntry")
public class AssayEntryController extends AbstractAssayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssayEntryController.class);

    @Autowired(required = false)
    private StudyNotebookService studyNotebookService;

    @GetMapping("/{assayId}/{templateId}/{entryName}")
    public NotebookEntry createEntry(@PathVariable("assayId") String assayId,
                                     @PathVariable("templateId") String templateId,
                                     @PathVariable("entryName") String entryName)
            throws RecordNotFoundException {
        Assay assay = getAssayFromIdentifier(assayId);
        LOGGER.info("Adding entry for assay: " + assayId);
        return Optional.ofNullable(studyNotebookService)
                .map(service -> service.createNotebook(assay, templateId, entryName)).
                        orElseThrow(() -> new RecordNotFoundException("Could not create new entry"));
    }

    @GetMapping("/templates")
    public List<BenchlingEntryTemplate> getEntryTemplates() {
        BenchlingEntryTemplate template1 = new BenchlingEntryTemplate();
        template1.setName("Cell Banking");
        template1.setId("tmpl_LwR3TYWL");
        BenchlingEntryTemplate template2 = new BenchlingEntryTemplate();
        template2.setName("Registration table");
        template2.setId("tmpl_eSXc9pam");

        return List.of(template1, template2);
    }
}
