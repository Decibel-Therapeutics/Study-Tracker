package com.decibeltx.studytracker.web.config;

import com.decibeltx.studytracker.core.model.Program;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DatabaseSeeds {

  private List<Program> programs = new ArrayList<>();

}
