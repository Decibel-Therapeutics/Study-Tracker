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

package com.decibeltx.studytracker.service;

import com.decibeltx.studytracker.exception.RecordNotFoundException;
import com.decibeltx.studytracker.model.Study;
import com.decibeltx.studytracker.model.StudyRelationship;
import com.decibeltx.studytracker.model.StudyRelationship.Type;
import com.decibeltx.studytracker.repository.StudyRelationshipRepository;
import com.decibeltx.studytracker.repository.StudyRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudyRelationshipService {

  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private StudyRelationshipRepository studyRelationshipRepository;

  public List<StudyRelationship> getStudyRelationships(Study study) {
    return studyRelationshipRepository.findBySourceStudyId(study.getId());
  }

  @Transactional
  public void addStudyRelationship(Study sourceStudy, Study targetStudy, Type type) {
    StudyRelationship sourceRelationship = new StudyRelationship(type, sourceStudy, targetStudy);
    Type targetType = Type.getInverse(type);
    StudyRelationship targetRelationship = new StudyRelationship(targetType, targetStudy, sourceStudy);
    studyRelationshipRepository.save(sourceRelationship);
    studyRelationshipRepository.save(targetRelationship);
    studyRepository.save(sourceStudy);
    studyRepository.save(targetStudy);
  }

  @Transactional
  public void removeStudyRelationship(Study sourceStudy, Study targetStudy) {

    Optional<StudyRelationship> optional = studyRelationshipRepository.findBySourceAndTargetStudyIds(
        sourceStudy.getId(), targetStudy.getId());
    if (optional.isPresent()) {
      studyRelationshipRepository.deleteById(optional.get().getId());
    } else {
      throw new RecordNotFoundException(String.format("No study relationship found for source study "
          + "%s and target study %s", sourceStudy.getCode(), targetStudy.getCode()));
    }

    optional = studyRelationshipRepository.findBySourceAndTargetStudyIds(
        targetStudy.getId(), sourceStudy.getId());
    if (optional.isPresent()) {
      studyRelationshipRepository.deleteById(optional.get().getId());
    } else {
      throw new RecordNotFoundException(String.format("No study relationship found for source study "
          + "%s and target study %s", targetStudy.getCode(), sourceStudy.getCode()));
    }

    studyRepository.save(sourceStudy);
    studyRepository.save(targetStudy);

  }

}
