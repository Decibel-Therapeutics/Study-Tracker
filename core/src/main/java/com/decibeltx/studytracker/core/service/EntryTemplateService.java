package com.decibeltx.studytracker.core.service;

import com.decibeltx.studytracker.core.model.EntryTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EntryTemplateService {

    Optional<EntryTemplate> findById(String id);

    List<EntryTemplate> findAll();

    void create(EntryTemplate entryTemplate);

    void update(EntryTemplate entryTemplate);

    void deleteAll();

    /**
     * Counting number of templates created before/after/between given dates.
     */
    long count();

    long countFromDate(Date startDate);

    long countBeforeDate(Date endDate);

    long countBetweenDates(Date startDate, Date endDate);
}
