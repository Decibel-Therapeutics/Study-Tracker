package com.decibeltx.studytracker.cli.executor;

import com.beust.jcommander.JCommander;
import com.decibeltx.studytracker.cli.argument.BaseArguments;
import com.decibeltx.studytracker.cli.argument.ImportArguments;
import com.decibeltx.studytracker.cli.config.AdminUserInitializer;
import com.decibeltx.studytracker.cli.config.UserAuthenticationService;
import com.decibeltx.studytracker.cli.exception.UnauthorizedException;
import com.decibeltx.studytracker.core.model.User;
import com.decibeltx.studytracker.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineProcessRunner implements CommandLineRunner {

  @Autowired
  private AdminUserInitializer adminUserInitializer;

  @Autowired
  private UserService userService;

  @Autowired
  private ImportExecutor importExecutor;

  @Autowired
  private UserAuthenticationService userAuthenticationService;

  @Override
  public void run(String[] args) throws Exception {

    if (userService.findAll().size() == 0) {
      adminUserInitializer.initializeAdminUser();
      return;
    }

    BaseArguments baseArguments = new BaseArguments();
    ImportArguments importArguments = new ImportArguments();

    JCommander jc = JCommander.newBuilder()
        .addObject(baseArguments)
        .addCommand(ImportArguments.COMMAND, importArguments)
        .build();

    jc.parse(args);

    // Help dialog
    if (baseArguments.isHelp()) {
      jc.usage();
      return;
    }

    // User authentication
    User user;

    String command = jc.getParsedCommand();

    if (command != null) {

      if (baseArguments.getUsername() == null || baseArguments.getPassword() == null) {
        throw new UnauthorizedException("Username and password must not be null.");
      }
      user = userAuthenticationService
          .authenticateUser(baseArguments.getUsername(), baseArguments.getPassword());

      // Import command
      if (ImportArguments.COMMAND.equals(command)) {
        importExecutor.execute(importArguments, user);
      } else {
        jc.usage();
      }
    } else {
      jc.usage();
    }

  }

}
