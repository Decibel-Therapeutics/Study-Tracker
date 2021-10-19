package com.decibeltx.studytracker.mapstruct.dto.elasticsearch;

import java.net.URL;
import lombok.Data;

@Data
public class ElasticsearchLinkDocument {
  private String label;
  private URL url;
}
