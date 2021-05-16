package com.decibeltx.studytracker.service;

import com.decibeltx.studytracker.exception.RecordNotFoundException;
import com.decibeltx.studytracker.model.NotebookEntryTemplate;
import com.decibeltx.studytracker.repository.EntryTemplateRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntryTemplateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryTemplateService.class);

    @Autowired
    private EntryTemplateRepository entryTemplateRepository;

    public Optional<NotebookEntryTemplate> findById(Long id) {
        return entryTemplateRepository.findById(id);
    }

    public List<NotebookEntryTemplate> findAll() {
        return entryTemplateRepository.findAll();
    }

    public List<NotebookEntryTemplate> findAllActive() {
        return findAll().stream().filter(NotebookEntryTemplate::isActive).collect(Collectors.toList());
    }

    public void create(NotebookEntryTemplate notebookEntryTemplate) {
        LOGGER.info("Creating new entry template with name: " + notebookEntryTemplate.getName());

        Date now = new Date();
        notebookEntryTemplate.setCreatedAt(now);
        notebookEntryTemplate.setUpdatedAt(now);
        entryTemplateRepository.save(notebookEntryTemplate);
    }

    public void update(NotebookEntryTemplate notebookEntryTemplate) {
        LOGGER.info("Updating entry template with name: " + notebookEntryTemplate.getName());

        assert notebookEntryTemplate.getId() != null;
        entryTemplateRepository.findById(notebookEntryTemplate.getId()).orElseThrow(
            RecordNotFoundException::new);
        entryTemplateRepository.save(notebookEntryTemplate);
    }
}
