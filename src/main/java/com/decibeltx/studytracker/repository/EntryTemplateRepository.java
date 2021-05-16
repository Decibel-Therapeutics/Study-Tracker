package com.decibeltx.studytracker.repository;

import com.decibeltx.studytracker.model.NotebookEntryTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryTemplateRepository extends JpaRepository<NotebookEntryTemplate, Long> {

}
