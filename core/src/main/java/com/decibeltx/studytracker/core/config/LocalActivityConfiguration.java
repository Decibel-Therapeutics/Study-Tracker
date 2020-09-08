package com.decibeltx.studytracker.core.config;

import com.decibeltx.studytracker.core.events.LocalActivityService;
import com.decibeltx.studytracker.core.service.ActivityService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "activity.mode", havingValue = "local")
public class LocalActivityConfiguration {

  @Bean
  public ActivityService localActivityService() {
    return new LocalActivityService();
  }

}
