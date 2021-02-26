package com.decibeltx.studytracker.core.service.impl;

import com.decibeltx.studytracker.core.exception.RecordNotFoundException;
import com.decibeltx.studytracker.core.model.EntryTemplate;
import com.decibeltx.studytracker.core.repository.EntryTemplateRepository;
import com.decibeltx.studytracker.core.service.EntryTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EntryTemplateServiceImpl implements EntryTemplateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryTemplateServiceImpl.class);

    @Autowired
    EntryTemplateRepository entryTemplateRepository;

    @Override
    public Optional<EntryTemplate> findById(String id) {
        return entryTemplateRepository.findById(id);
    }

    @Override
    public List<EntryTemplate> findAll() {
        return entryTemplateRepository.findAll();
    }

    @Override
    public void create(EntryTemplate entryTemplate) {
        LOGGER.info("Creating new entry template with name: " + entryTemplate.getName());

        entryTemplate.setCreatedAt(new Date());
        entryTemplate.setUpdatedAt(new Date());
        entryTemplateRepository.insert(entryTemplate);
    }

    @Override
    public void update(EntryTemplate entryTemplate) {
        LOGGER.info("Updating entry template with name: " + entryTemplate.getName());

        assert entryTemplate.getId() != null;
        entryTemplateRepository.findById(entryTemplate.getId()).orElseThrow(RecordNotFoundException::new);
        entryTemplateRepository.save(entryTemplate);
    }

    @Override
    public void delete(EntryTemplate entryTemplate) {
        LOGGER.info("Deleting entry template with name: " + entryTemplate.getName());

        assert entryTemplate.getId() != null;
        entryTemplateRepository.findById(entryTemplate.getId()).orElseThrow(RecordNotFoundException::new);
        entryTemplateRepository.delete(entryTemplate);
    }

    @Override
    public long count() {
        return entryTemplateRepository.count();
    }

    @Override
    public long countFromDate(Date startDate) {
        return entryTemplateRepository.countByCreatedAtAfter(startDate);
    }

    @Override
    public long countBeforeDate(Date endDate) {
        return entryTemplateRepository.countByCreatedAtBefore(endDate);
    }

    @Override
    public long countBetweenDates(Date startDate, Date endDate) {
        return entryTemplateRepository.countByCreatedAtBetween(startDate, endDate);
    }
}
