package com.decibeltx.studytracker.search.elasticsearch;

import com.decibeltx.studytracker.mapstruct.dto.elasticsearch.ElasticsearchStudyDocument;
import com.decibeltx.studytracker.mapstruct.mapper.ElasticsearchDocumentMapper;
import com.decibeltx.studytracker.model.Study;
import com.decibeltx.studytracker.search.SearchService;
import com.decibeltx.studytracker.search.StudySearchHits;
import java.util.Collection;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

public class ElasticsearchSearchService implements SearchService<ElasticsearchStudyDocument, Long> {

  @Autowired
  private ElasticsearchRestTemplate elasticsearchRestTemplate;

  @Autowired
  private StudyIndexRepository studyIndexRepository;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private ElasticsearchDocumentMapper documentMapper;

  @Value("${elasticsearch.index}")
  private String indexName;

  @Override
  public StudySearchHits<ElasticsearchStudyDocument> search(String keyword) {
    NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
        .withQuery(new QueryStringQueryBuilder(keyword))
        .build();
    SearchHits<ElasticsearchStudyDocument> hits = elasticsearchRestTemplate.search(
        nativeSearchQuery, ElasticsearchStudyDocument.class, IndexCoordinates.of(indexName));
    return StudySearchHits.fromElasticsearchHits(hits);
  }

  @Override
  public StudySearchHits<ElasticsearchStudyDocument> search(String keyword, String field) {
    NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
        .withQuery(new QueryStringQueryBuilder(keyword).field(field))
        .build();
    SearchHits<ElasticsearchStudyDocument> hits = elasticsearchRestTemplate.search(
        nativeSearchQuery, ElasticsearchStudyDocument.class, IndexCoordinates.of(indexName));
    return StudySearchHits.fromElasticsearchHits(hits);
  }

  @Override
  public void indexStudy(Study study) {
    ElasticsearchStudyDocument document = documentMapper.fromStudy(study);
    studyIndexRepository.save(document);
  }

  @Override
  public void indexStudies(Collection<Study> studies) {
    for (Study study: studies) {
      this.indexStudy(study);
    }
  }
}
