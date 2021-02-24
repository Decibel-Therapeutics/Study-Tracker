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
import java.util.Optional;

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
        return entryTemplateService.findAll();
    }

    @PostMapping("")
    public HttpEntity<EntryTemplate> createTemplate(@RequestBody EntryTemplate entryTemplate) {
        LOGGER.info("Creating new entry template : " + entryTemplate.toString());

        // Get authenticated user
        String username = UserAuthenticationUtils
                .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
        User user = userService.findByUsername(username)
                .orElseThrow(RecordNotFoundException::new);

        entryTemplate.setCreatedBy(user);

        entryTemplateService.create(entryTemplate);

        return new ResponseEntity<>(entryTemplate, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public HttpEntity<EntryTemplate> updateTemplate(@PathVariable("id") String templateId,
                                                    @RequestParam("active") boolean active) {
        Optional<EntryTemplate> entryTemplateOptional = entryTemplateService.findById(templateId);
        if (entryTemplateOptional.isEmpty()) {
            throw new RecordNotFoundException("Template not found: " + templateId);
        }

        EntryTemplate entryTemplate = entryTemplateOptional.get();

        String username = UserAuthenticationUtils
                .getUsernameFromAuthentication(SecurityContextHolder.getContext().getAuthentication());
        User user = userService.findByUsername(username)
                .orElseThrow(RecordNotFoundException::new);

        entryTemplate.setLastModifiedBy(user);
        entryTemplate.setActive(active);
        entryTemplateService.update(entryTemplate);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("")
    public void deleteTemplates() {
        entryTemplateService.deleteAll();
    }

}
