package com.decibeltx.studytracker.web.config;

import com.decibeltx.studytracker.core.exception.StudyTrackerException;
import com.decibeltx.studytracker.core.model.Program;
import com.decibeltx.studytracker.core.service.ProgramService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeedingRunner implements ApplicationRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSeedingRunner.class);

  private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
  @Autowired
  private MongoTemplate mongoTemplate;
  @Autowired
  private ProgramService programService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    if (args.containsOption("drop-database")) {
      LOGGER.warn("Wiping database...");
      mongoTemplate.getDb().drop();
    }
    if (args.containsOption("seeds") && !args.getOptionValues("seeds").isEmpty()) {
      Path workingDir = Paths.get(System.getProperty("user.dir"));
      Path seedFilePath = workingDir.resolve(args.getOptionValues("seeds").get(0));
      File seedFile = seedFilePath.toFile();
      if (seedFile.exists() && seedFile.isFile() && seedFile.canRead()) {
        LOGGER.info("Importing database seeds from file: " + seedFile.getAbsolutePath());
        DatabaseSeeds seeds;
        try {
          seeds = objectMapper.readValue(seedFile, DatabaseSeeds.class);
        } catch (Exception e) {
          throw new IOException("Failed to parse seed file: " + seedFile.getAbsolutePath());
        }
        loadSeedData(seeds);
      } else {
        throw new IOException("Cannot read seed file: " + seedFilePath.toString());
      }
    }
  }

  private void loadSeedData(DatabaseSeeds seeds) throws StudyTrackerException {
    if (!seeds.getPrograms().isEmpty()) {
      LOGGER.info("Inserting program records...");
      for (Program program : seeds.getPrograms()) {
        programService.create(program);
      }
    }
    LOGGER.info("Database seeding complete.");
  }

}
