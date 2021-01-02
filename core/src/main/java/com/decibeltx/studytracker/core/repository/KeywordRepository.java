package com.decibeltx.studytracker.core.repository;

import com.decibeltx.studytracker.core.model.Keyword;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KeywordRepository extends MongoRepository<Keyword, String> {

  List<Keyword> findByKeyword(String keyword);

  List<Keyword> findByCategory(String category);

  Optional<Keyword> findByKeywordAndCategory(String keyword, String category);

  List<Keyword> findByKeywordLike(String keyword);

  List<Keyword> findByCategoryAndKeywordLike(String category, String keyword);

}
