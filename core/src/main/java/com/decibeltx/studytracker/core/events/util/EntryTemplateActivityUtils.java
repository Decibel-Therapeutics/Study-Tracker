package com.decibeltx.studytracker.core.events.util;

import com.decibeltx.studytracker.core.model.Activity;
import com.decibeltx.studytracker.core.model.Activity.Reference;
import com.decibeltx.studytracker.core.model.EntryTemplate;
import com.decibeltx.studytracker.core.model.EventType;
import com.decibeltx.studytracker.core.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EntryTemplateActivityUtils {

    private static Activity getActivity(EntryTemplate entryTemplate, User triggeredBy, EventType eventType) {
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
        return getActivity(entryTemplate, triggeredBy, EventType.NEW_ENTRY_TEMPLATE);
    }

    public static Activity fromUpdatedEntryTemplate(EntryTemplate entryTemplate, User triggeredBy) {
        return getActivity(entryTemplate, triggeredBy, EventType.UPDATED_ENTRY_TEMPLATE);
    }
}
