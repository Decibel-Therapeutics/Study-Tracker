package com.decibeltx.studytracker.web.security;

import com.decibeltx.studytracker.core.model.User;
import com.decibeltx.studytracker.core.service.UserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CombinedUserDetailsService implements UserDetailsService, SAMLUserDetailsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CombinedUserDetailsService.class);

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userService.findByUsername(username).orElse(null);
  }

  @Override
  public Object loadUserBySAML(SAMLCredential samlCredential) throws UsernameNotFoundException {
    System.out.println(samlCredential.toString());
    LOGGER.info(String.format("Looking up UserDetails by SAML credentials: %s",
        samlCredential.getNameID().getValue()));
    Optional<User> optional = userService.findByUsername(samlCredential.getNameID().getValue());
    if (optional.isPresent()) {
      return optional.get();
    } else {
      throw new UsernameNotFoundException(
          "Cannot find user: " + samlCredential.getNameID().getValue());
    }
  }
}
