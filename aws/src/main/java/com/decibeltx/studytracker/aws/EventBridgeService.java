package com.decibeltx.studytracker.aws;

import com.decibeltx.studytracker.core.exception.StudyTrackerException;
import com.decibeltx.studytracker.core.model.Activity;
import com.decibeltx.studytracker.core.service.EventsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResultEntry;

public class EventBridgeService implements EventsService {

  private final EventBridgeClient client;

  private final ObjectMapper objectMapper;

  private final String eventBusName;

  public EventBridgeService(EventBridgeClient client, ObjectMapper objectMapper,
      String eventBusName) {
    this.client = client;
    this.objectMapper = objectMapper;
    this.eventBusName = eventBusName;
  }

  @Override
  public void dispatchEvent(Activity activity) {
    String json;
    try {
      json = objectMapper.writeValueAsString(activity.getData());
    } catch (JsonProcessingException e) {
      throw new StudyTrackerException(e);
    }
    PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
        .eventBusName(eventBusName)
        .source("study-tracker")
        .detailType(activity.getEventType().toString())
        .detail(json)
        .build();
    PutEventsRequest request = PutEventsRequest.builder()
        .entries(entry)
        .build();
    PutEventsResponse response = client.putEvents(request);
    for (PutEventsResultEntry resultEntry : response.entries()) {
      System.out.println(resultEntry.toString());
    }

  }
}
