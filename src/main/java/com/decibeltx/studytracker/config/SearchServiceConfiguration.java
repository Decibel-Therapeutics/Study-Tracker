package com.decibeltx.studytracker.config;

import com.decibeltx.studytracker.model.Study;
import com.decibeltx.studytracker.repository.StudyRepository;
import com.decibeltx.studytracker.search.SearchService;
import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Configuration
@ConditionalOnProperty(name = "search.mode", havingValue = "")
@EnableScheduling
public class SearchServiceConfiguration {

  public static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceConfiguration.class);

  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private SearchService<?, ?> searchService;

  /**
   * Indexes all studies that have been updated within the past two hours. Runs on a schedule every
   *   other hour.
   */
  @Scheduled(cron = "0 0 */2 * * *")
  public void scheduledStudyIndex() {
    LOGGER.info("Running scheduled study search indexing...");
    int count = 0;
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, -130);
    for (Study study: studyRepository.findByUpdatedAtAfter(calendar.getTime())){
      searchService.indexStudy(study);
      count = count + 1;
    }
    LOGGER.info("Study indexing complete. Indexed {} studies", count);
  }

  @Component
  public static class ApplicationStartupStudyIndexer implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartupStudyIndexer.class);

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private SearchService<?, ?> searchService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
      LOGGER.info("Running startup full study search indexing...");
      int count = 0;
      for (Study study: studyRepository.findAllWithDetails()) {
        searchService.indexStudy(study);
        count = count + 1;
      }
      LOGGER.info("Study indexing complete. Indexed {} studies", count);
    }
  }

}
