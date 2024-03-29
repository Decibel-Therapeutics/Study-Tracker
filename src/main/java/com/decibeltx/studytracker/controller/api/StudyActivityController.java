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

package com.decibeltx.studytracker.controller.api;

import com.decibeltx.studytracker.mapstruct.dto.ActivitySummaryDto;
import com.decibeltx.studytracker.mapstruct.mapper.ActivityMapper;
import com.decibeltx.studytracker.model.Study;
import com.decibeltx.studytracker.service.ActivityService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/study/{studyId}/activity")
@RestController
public class StudyActivityController extends AbstractStudyController {

  @Autowired
  private ActivityService activityService;

  @Autowired
  private ActivityMapper activityMapper;

  @GetMapping("")
  public List<ActivitySummaryDto> getStudyActivity(@PathVariable("studyId") String studyId) {
    Study study = this.getStudyFromIdentifier(studyId);
    return activityMapper.toActivitySummaryList(activityService.findByStudy(study));
  }

}
