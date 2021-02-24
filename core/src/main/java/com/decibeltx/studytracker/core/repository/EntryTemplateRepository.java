package com.decibeltx.studytracker.core.repository;

import com.decibeltx.studytracker.core.model.EntryTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

public interface EntryTemplateRepository extends MongoRepository<EntryTemplate, String> {
    long countByCreatedAtBefore(Date date);

    long countByCreatedAtAfter(Date date);

    long countByCreatedAtBetween(Date startDate, Date endDate);
}
