package com.decibeltx.studytracker.web.security;

import com.decibeltx.studytracker.core.model.User;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DatabaseAuthProvider implements AuthenticationProvider {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseAuthProvider.class);

  private final CombinedUserDetailsService combinedUserDetailsService;

  private final PasswordEncoder passwordEncoder;

  public DatabaseAuthProvider(
      CombinedUserDetailsService combinedUserDetailsService,
      PasswordEncoder passwordEncoder) {
    this.combinedUserDetailsService = combinedUserDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    User user = (User) combinedUserDetailsService
        .loadUserByUsername(authentication.getPrincipal().toString());
    String pw =
        authentication.getCredentials() == null ? null : authentication.getCredentials().toString();
    if (passwordEncoder.matches(pw, user.getPassword())) {
      LOGGER.warn("User successfully logged in: {}", user.getUsername());
      return new UsernamePasswordAuthenticationToken(user.getUsername(), pw,
          Collections.emptyList());
    } else {
      LOGGER.error("User failed to log in: {}", user.getUsername());
      throw new BadCredentialsException("Invalid credentials");
    }
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return aClass.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
  }
}
