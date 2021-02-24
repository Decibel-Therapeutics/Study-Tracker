package com.decibeltx.studytracker.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/saml")
public class SingleSignOnController {

  @Value("${saml.ssoUrl}")
  private String ssoUrl;

  @GetMapping("loginredirect")
  public String loginRedirect() {
    return "redirect:" + ssoUrl;
  }

  @GetMapping("/discovery")
  public String idpSelection() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth instanceof AnonymousAuthenticationToken) {
      return "redirect:/login";
    } else {
      return "redirect:/";
    }
  }

}
