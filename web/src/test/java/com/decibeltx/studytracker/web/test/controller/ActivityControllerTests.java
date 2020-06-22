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

package com.decibeltx.studytracker.web.test.controller;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.decibeltx.studytracker.core.example.ExampleDataGenerator;
import com.decibeltx.studytracker.core.model.Status;
import com.decibeltx.studytracker.core.model.Study;
import com.decibeltx.studytracker.core.service.StudyService;
import com.decibeltx.studytracker.web.test.TestApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles({"test", "example"})
public class ActivityControllerTests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ExampleDataGenerator exampleDataGenerator;
  @Autowired
  private StudyService studyService;

  @Before
  public void doBefore() {
    exampleDataGenerator.populateDatabase();
  }

  @Test
  public void getAllActivityTest() throws Exception {

    Study study = studyService.findAll().get(0);
    studyService.updateStatus(study, Status.ON_HOLD);

    mockMvc.perform(get("/api/activity"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0]", hasKey("id")))
        .andExpect(jsonPath("$[0]", hasKey("eventType")))
        .andExpect(jsonPath("$[0]", hasKey("date")))
        .andExpect(jsonPath("$[0]", hasKey("data")));

  }

  @Test
  public void getSortedActivityTest() throws Exception {

    Study study = studyService.findAll().get(0);
    studyService.updateStatus(study, Status.ON_HOLD);
    studyService.updateStatus(study, Status.COMPLETE);

    mockMvc.perform(get("/api/activity?sort=date,desc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0]", hasKey("eventType")))
        .andExpect(jsonPath("$[0].eventType", is("STUDY_STATUS_CHANGED")))
        .andExpect(jsonPath("$[0]", hasKey("data")))
        .andExpect(jsonPath("$[0].data", hasKey("newStatus")))
        .andExpect(jsonPath("$[0].data.newStatus", is("COMPLETE")))
        .andExpect(jsonPath("$[1]", hasKey("eventType")))
        .andExpect(jsonPath("$[1].eventType", is("STUDY_STATUS_CHANGED")))
        .andExpect(jsonPath("$[1]", hasKey("data")))
        .andExpect(jsonPath("$[1].data", hasKey("newStatus")))
        .andExpect(jsonPath("$[1].data.newStatus", is("ON_HOLD")));

  }

  @Test
  public void getPagedActivityTest() throws Exception {

    Study study = studyService.findAll().get(0);
    studyService.updateStatus(study, Status.ON_HOLD);
    studyService.updateStatus(study, Status.COMPLETE);

    mockMvc.perform(get("/api/activity?size=2&page=0&sort=date,desc"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasKey("content")))
        .andExpect(jsonPath("$", hasKey("pageable")))
        .andExpect(jsonPath("$", hasKey("size")))
        .andExpect(jsonPath("$.size", is(2)))
        .andExpect(jsonPath("$", hasKey("number")))
        .andExpect(jsonPath("$.number", is(0)))
        .andExpect(jsonPath("$", hasKey("sort")))
        .andExpect(jsonPath("$.sort", hasKey("sorted")))
        .andExpect(jsonPath("$.sort.sorted", is(true)))
        .andExpect(jsonPath("$.content[0]", hasKey("eventType")))
        .andExpect(jsonPath("$.content[0].eventType", is("STUDY_STATUS_CHANGED")))
        .andExpect(jsonPath("$.content[0]", hasKey("data")))
        .andExpect(jsonPath("$.content[0].data", hasKey("newStatus")))
        .andExpect(jsonPath("$.content[0].data.newStatus", is("COMPLETE")))
        .andExpect(jsonPath("$.content[1]", hasKey("eventType")))
        .andExpect(jsonPath("$.content[1].eventType", is("STUDY_STATUS_CHANGED")))
        .andExpect(jsonPath("$.content[1]", hasKey("data")))
        .andExpect(jsonPath("$.content[1].data", hasKey("newStatus")))
        .andExpect(jsonPath("$.content[1].data.newStatus", is("ON_HOLD")));

  }

}
