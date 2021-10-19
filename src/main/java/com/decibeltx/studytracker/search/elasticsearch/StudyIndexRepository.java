package com.decibeltx.studytracker.search.elasticsearch;

import com.decibeltx.studytracker.mapstruct.dto.elasticsearch.ElasticsearchStudyDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StudyIndexRepository
    extends ElasticsearchRepository<ElasticsearchStudyDocument, String> {

}
