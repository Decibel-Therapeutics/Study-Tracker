package com.decibeltx.studytracker.search.elasticsearch;

import com.decibeltx.studytracker.mapstruct.dto.elasticsearch.ElasticsearchStudyDocument;
import java.util.Collection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StudyIndexRepository
    extends ElasticsearchRepository<ElasticsearchStudyDocument, String> {

  @Query("{\"multi_match\": {\"query\": \"?0\" }}")
  SearchHits<ElasticsearchStudyDocument> findDocumentsByKeyword(String keyword);

  @Query("{\"multi_match\": {\"query\": \"?0\" }}")
  SearchPage<ElasticsearchStudyDocument> findDocumentsByKeyword(String keyword, Pageable pageable);

  @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": \"?1\" }}")
  SearchHits<ElasticsearchStudyDocument> findDocumentsByKeywordAndField(String keyword, Collection<String> fields);

  @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": \"?1\" }}")
  SearchPage<ElasticsearchStudyDocument> findDocumentsByKeywordAndField(String keyword, Collection<String> fields, Pageable pageable);

}
