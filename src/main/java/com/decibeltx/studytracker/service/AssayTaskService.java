package com.decibeltx.studytracker.service;

import com.decibeltx.studytracker.model.Assay;
import com.decibeltx.studytracker.model.AssayTask;
import com.decibeltx.studytracker.model.Task;
import com.decibeltx.studytracker.repository.AssayRepository;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssayTaskService {

  @Autowired
  private AssayRepository assayRepository;

  public Set<AssayTask> findAssayTasks(Assay assay) {
    return assay.getTasks();
  }

  @Transactional
  public void addAssayTask(AssayTask task, Assay assay) {
    Date now = new Date();
    task.setCreatedAt(now);
    task.setUpdatedAt(now);
    if (task.getOrder() == null) {
      task.setOrder(assay.getTasks().size());
    }
    assay.getTasks().add(task);
    assayRepository.save(assay);
  }

  @Transactional
  public void updateAssayTask(AssayTask task, Assay assay) {
    for (Task t : assay.getTasks()) {
      if (t.getLabel().equals(task.getLabel())) {
        t.setStatus(task.getStatus());
        t.setUpdatedAt(new Date());
      }
    }
    assayRepository.save(assay);
  }

  @Transactional
  public void deleteAssayTask(AssayTask task, Assay assay) {
    Set<AssayTask> tasks = assay.getTasks().stream()
        .filter(t -> !t.getLabel().equals(task.getLabel()))
        .collect(Collectors.toSet());
    assay.setTasks(tasks);
    assayRepository.save(assay);
  }

}
