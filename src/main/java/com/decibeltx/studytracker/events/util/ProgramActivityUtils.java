package com.decibeltx.studytracker.events.util;

import com.decibeltx.studytracker.events.EventType;
import com.decibeltx.studytracker.model.Activity;
import com.decibeltx.studytracker.model.ActivityReference;
import com.decibeltx.studytracker.model.Program;
import com.decibeltx.studytracker.model.User;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProgramActivityUtils {

  public static Activity fromNewProgram(Program program, User triggeredBy) {
    Activity activity = new Activity();
    activity.setReference(ActivityReference.PROGRAM);
    activity.setReferenceId(program.getId());
    activity.setEventType(EventType.NEW_PROGRAM);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("program", EntityViewUtils.createProgramView(program));
    activity.setData(data);
    return activity;
  }

  public static Activity fromUpdatedProgram(Program program, User triggeredBy) {
    Activity activity = new Activity();
    activity.setReference(ActivityReference.PROGRAM);
    activity.setReferenceId(program.getId());
    activity.setEventType(EventType.UPDATED_PROGRAM);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("program", EntityViewUtils.createProgramView(program));
    activity.setData(data);
    return activity;
  }

  public static Activity fromDeletedProgram(Program program, User triggeredBy) {
    Activity activity = new Activity();
    activity.setReference(ActivityReference.PROGRAM);
    activity.setReferenceId(program.getId());
    activity.setEventType(EventType.DELETED_PROGRAM);
    activity.setDate(new Date());
    activity.setUser(triggeredBy);
    Map<String, Object> data = new HashMap<>();
    data.put("program", EntityViewUtils.createProgramView(program));
    activity.setData(data);
    return activity;
  }

}
