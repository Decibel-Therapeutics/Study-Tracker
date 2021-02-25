package com.decibeltx.studytracker.web.controller.api;

import com.decibeltx.studytracker.core.exception.RecordNotFoundException;
import com.decibeltx.studytracker.core.model.EntryTemplate;
import com.decibeltx.studytracker.core.model.User;
import com.decibeltx.studytracker.core.service.EntryTemplateService;
import com.decibeltx.studytracker.core.service.UserService;
import com.decibeltx.studytracker.web.controller.UserAuthenticationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entryTemplate")
public class EntryTemplateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryTemplateController.class);

    @Autowired
    private EntryTemplateService entryTemplateService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<EntryTemplate> getEntryTemplates() {
        LOGGER.info("Getting all entry templates");

        return entryTemplateService.findAll();
    }

    private User getAuthenticatedUser() throws RecordNotFoundException {
        String username = UserAuthenticationUtils
                .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
        return userService.findByUsername(username)
                .orElseThrow(RecordNotFoundException::new);
    }

    @PostMapping("")
    public HttpEntity<EntryTemplate> createTemplate(@RequestBody EntryTemplate entryTemplate)
            throws RecordNotFoundException {
        LOGGER.info("Creating new entry template : " + entryTemplate.toString());

        entryTemplate.setCreatedBy(getAuthenticatedUser());
        entryTemplateService.create(entryTemplate);

        return new ResponseEntity<>(entryTemplate, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/status")
    public HttpEntity<EntryTemplate> updateTemplate(@PathVariable("id") String id,
                                                    @RequestParam("active") boolean active)
            throws RecordNotFoundException {
        LOGGER.info("Updating template with id: " + id);

        EntryTemplate entryTemplate = entryTemplateService
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Template not found: " + id));

        entryTemplate.setLastModifiedBy(getAuthenticatedUser());
        entryTemplate.setActive(active);
        entryTemplateService.update(entryTemplate);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("")
    public void deleteTemplates() {
        LOGGER.info("Deleting all templates");

        entryTemplateService.deleteAll();
    }

}
