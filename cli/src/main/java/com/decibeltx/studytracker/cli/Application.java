package com.decibeltx.studytracker.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
