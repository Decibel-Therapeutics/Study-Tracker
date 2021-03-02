package com.decibeltx.studytracker.core.service;

import com.decibeltx.studytracker.core.model.EntryTemplate;

import java.util.List;
import java.util.Optional;

public interface EntryTemplateService {

    Optional<EntryTemplate> findById(String id);

    List<EntryTemplate> findAll();

    List<EntryTemplate> findAllActive();

    void create(EntryTemplate entryTemplate);

    void update(EntryTemplate entryTemplate);
}
