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

package com.decibeltx.studytracker.core.service.impl;

import com.decibeltx.studytracker.core.exception.RecordNotFoundException;
import com.decibeltx.studytracker.core.model.Activity;
import com.decibeltx.studytracker.core.model.Assay;
import com.decibeltx.studytracker.core.model.EventType;
import com.decibeltx.studytracker.core.model.Study;
import com.decibeltx.studytracker.core.model.User;
import com.decibeltx.studytracker.core.model.EntryTemplate;
import com.decibeltx.studytracker.core.model.Program;
import com.decibeltx.studytracker.core.repository.ActivityRepository;
import com.decibeltx.studytracker.core.repository.StudyRepository;
import com.decibeltx.studytracker.core.service.ActivityService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {

  @Autowired
  private ActivityRepository activityRepository;

  @Autowired
  private StudyRepository studyRepository;

  @Override
  public List<Activity> findAll() {
    return activityRepository.findAll();
  }

  @Override
  public List<Activity> findAll(Sort sort) {
    return activityRepository.findAll(sort);
  }

  @Override
  public Page<Activity> findAll(Pageable pageable) {
    return activityRepository.findAll(pageable);
  }

  @Override
  public Optional<Activity> findById(String id) {
    return activityRepository.findById(id);
  }

  @Override
  public List<Activity> findByStudy(Study study) {
    List<Activity> activityList = activityRepository.findByStudyId(study.getId());
    for (Assay assay : study.getAssays()) {
      activityList.addAll(this.findByAssay(assay));
    }
    return activityList;
  }

  @Override
  public List<Activity> findByAssay(Assay assay) {
    return activityRepository.findByAssayId(assay.getId());
  }

  @Override
  public List<Activity> findByProgram(Program program) {
    List<Activity> activities = new ArrayList<>();
    for (Study study : studyRepository.findByProgramId(program.getId())) {
      List<Activity> activityList = this.findByStudy(study);
      activities.addAll(activityList);
    }
    return activities;
  }

  @Override
  public List<Activity> findByEventType(EventType type) {
    return activityRepository.findByEventType(type);
  }

  @Override
  public List<Activity> findByUser(User user) {
    return activityRepository.findByUserId(user.getId());
  }

  @Override
  public List<Activity> findByEntryTemplate(EntryTemplate entryTemplate) {
    return activityRepository.findByEntryTemplateId(entryTemplate.getId());
  }

  @Override
  public Activity create(Activity activity) {
    return activityRepository.save(activity);
  }

  @Override
  public Activity update(Activity activity) {
    if (activityRepository.existsById(activity.getId())) {
      activityRepository.save(activity);
      return activity;
    }
    throw new RecordNotFoundException(activity.getId());
  }

  @Override
  public void delete(Activity activity) {
    if (activityRepository.existsById(activity.getId())) {
      activityRepository.delete(activity);
    }
    throw new RecordNotFoundException(activity.getId());
  }

  @Override
  public void deleteStudyActivity(Study study) {
    for (Activity activity : this.findByStudy(study)) {
      this.delete(activity);
    }
  }

  @Override
  public long count() {
    return activityRepository.count();
  }

  @Override
  public long countFromDate(Date startDate) {
    return activityRepository.countByDateAfter(startDate);
  }

  @Override
  public long countBeforeDate(Date endDate) {
    return activityRepository.countByDateBefore(endDate);
  }

  @Override
  public long countBetweenDates(Date startDate, Date endDate) {
    return activityRepository.countByDateBetween(startDate, endDate);
  }

  @Override
  public long countCompletedStudiesFromDate(Date date) {
    return activityRepository.findCompletedStudiesAfterDate(date).size();
  }
}
