package com.decibeltx.studytracker.core.events;

import com.decibeltx.studytracker.core.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

public class LocalActivityListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalActivityListener.class);

  @Autowired
  private ActivityService activityService;

  @EventListener
  @Order(1)
  public void onApplicationEvent(StudyTrackerEvent event) {
    LOGGER.info("Logging new event: " + event.toString());
    activityService.create(event.getActivity());
  }

}
