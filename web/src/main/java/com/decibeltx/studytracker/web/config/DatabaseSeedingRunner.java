package com.decibeltx.studytracker.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeedingRunner implements ApplicationRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSeedingRunner.class);

  @Override
  public void run(ApplicationArguments args) throws Exception {
    for (String arg : args.getNonOptionArgs()) {
      LOGGER.info("Non option arg: " + arg);
    }
    for (String arg : args.getOptionNames()) {
      LOGGER.info("Option name: " + arg);
      LOGGER.info("Option value: " + args.getOptionValues(arg).toString());
    }
    for (String arg : args.getSourceArgs()) {
      LOGGER.info("Source arg: " + arg);
    }
  }
}
