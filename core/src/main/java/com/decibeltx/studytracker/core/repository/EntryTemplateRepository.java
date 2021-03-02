package com.decibeltx.studytracker.core.repository;

import com.decibeltx.studytracker.core.model.EntryTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntryTemplateRepository extends MongoRepository<EntryTemplate, String>  {

}
