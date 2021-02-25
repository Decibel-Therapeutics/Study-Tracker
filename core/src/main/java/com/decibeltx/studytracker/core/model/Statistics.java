package com.decibeltx.studytracker.core.model;

import java.util.Date;
import lombok.Data;

@Data
public class Statistics {

  /* Timeframe */
  private Date startDate;

  private Date endDate;

  /* Total counts */
  private long studyCount;

  private long programCount;

  private long assayCount;

  private long userCount;

  private long activityCount;

  private long entryTemplateCount;

  /* Specific counts */
  private long activeUserCount;

  private long newStudyCount;

  private long completedStudyCount;

  private long newAssayCount;

  private long completedAssayCount;

}
