package com.decibeltx.studytracker.cli.config;

import com.decibeltx.studytracker.core.model.User;
import com.decibeltx.studytracker.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserInitializer.class);

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public void initializeAdminUser() {
    LOGGER.info("No users present in the Study Tracker database. Initializing admin user...");
    String username = "admin";
    String password = "password";
    User user = new User();
    user.setActive(true);
    user.setDisplayName("Study Tracker Admin");
    user.setUsername(username);
    user.setEmail("admin@stucytracker.com");
    user.setAdmin(true);
    user.setPassword(passwordEncoder.encode(password));
    userService.create(user);
    LOGGER.info(String
        .format("Created admin user with username '%s' and password '%s'", username, password));
  }

}
