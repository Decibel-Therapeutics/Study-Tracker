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

package com.decibeltx.studytracker.controller;

import com.decibeltx.studytracker.mapstruct.dto.StudySlimDto;
import com.decibeltx.studytracker.mapstruct.dto.UserSlimDto;
import com.decibeltx.studytracker.mapstruct.mapper.StudyMapper;
import com.decibeltx.studytracker.mapstruct.mapper.UserMapper;
import com.decibeltx.studytracker.service.StudyService;
import com.decibeltx.studytracker.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/autocomplete")
@ApiIgnore
public class AutocompleteController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private StudyService studyService;

  @Autowired
  private StudyMapper studyMapper;

  @GetMapping("/user")
  public List<UserSlimDto> userSearch(@RequestParam("q") String keyword) {
    return userMapper.toUserSlimList(userService.search(keyword));
  }

  @GetMapping("/study")
  public List<StudySlimDto> studySearch(@RequestParam("q") String keyword) {
    return studyMapper.toStudySlimList(studyService.search(keyword));
  }

}
