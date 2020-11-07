package com.decibeltx.studytracker.benchling.eln.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BenchlingFolderList {

  private List<BenchlingFolder> folders = new ArrayList<>();

}
