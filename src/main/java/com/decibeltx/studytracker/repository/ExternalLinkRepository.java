package com.decibeltx.studytracker.repository;

import com.decibeltx.studytracker.model.ExternalLink;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExternalLinkRepository extends JpaRepository<ExternalLink, Long> {

  @Query("select l from ExternalLink l where l.study.id = ?1")
  List<ExternalLink> findByStudyId(Long studyId);

}
