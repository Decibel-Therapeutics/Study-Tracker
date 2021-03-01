package com.decibeltx.studytracker.core.events.util;

import com.decibeltx.studytracker.core.model.Activity;
import com.decibeltx.studytracker.core.model.Activity.Reference;
import com.decibeltx.studytracker.core.model.EntryTemplate;
import com.decibeltx.studytracker.core.model.EventType;
import com.decibeltx.studytracker.core.model.User;

import java.util.Collections;
import java.util.Date;

public class EntryTemplateActivityUtils {

    private static Activity createActivity(EntryTemplate entryTemplate, User triggeredBy, EventType eventType) {
        Activity activity = new Activity();
        activity.setReference(Reference.ENTRY_TEMPLATE);
        activity.setReferenceId(entryTemplate.getId());
        activity.setEventType(eventType);
        activity.setDate(new Date());
        activity.setUser(triggeredBy);
        activity.setData(Collections.singletonMap("entryTemplate", entryTemplate));
        return activity;
    }

    public static Activity fromNewEntryTemplate(EntryTemplate entryTemplate, User triggeredBy) {
        return createActivity(entryTemplate, triggeredBy, EventType.NEW_ENTRY_TEMPLATE);
    }

    public static Activity fromUpdatedEntryTemplate(EntryTemplate entryTemplate, User triggeredBy) {
        return createActivity(entryTemplate, triggeredBy, EventType.UPDATED_ENTRY_TEMPLATE);
    }
}
