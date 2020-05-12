/*
 * Copyright 2020 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.decibeltx.studytracker.core.events;

import com.decibeltx.studytracker.core.exception.RecordNotFoundException;
import com.decibeltx.studytracker.core.model.Activity;
import com.decibeltx.studytracker.core.model.Study;
import com.decibeltx.studytracker.core.repository.ActivityRepository;
import com.decibeltx.studytracker.core.repository.StudyRepository;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Invoked on any {@link StudyEvent} event. Creates a new {@link Activity} record to associate with
 * the target study.
 */
@Component
public class StudyActivityListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(StudyActivityListener.class);

  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private ActivityRepository activityRepository;

  @EventListener
  @Order(1)
  public void onApplicationEvent(StudyEvent studyEvent) {
    LOGGER.info("Logging new study event: " + studyEvent.toString());
    Activity activity = new Activity();
    activity.setAction(studyEvent.getType().toString());
    activity.setUser(studyEvent.getUser());
    activity.setDate(new Date());
    activity.setStudy(studyEvent.getStudy());
    activity.setData(studyEvent.getData());
    activityRepository.save(activity);
    Study study = studyRepository.findById(studyEvent.getStudy().getId())
        .orElseThrow(RecordNotFoundException::new);
    study.getActivity().add(activity);
    study.setUpdatedAt(new Date());
    studyRepository.save(study);
  }
}
