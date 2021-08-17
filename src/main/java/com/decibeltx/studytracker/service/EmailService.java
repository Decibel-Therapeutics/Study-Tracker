package com.decibeltx.studytracker.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;

  @Value("#{servletContext.contextPath}")
  private String contextPath;

  @Value("${email.outgoing-email-address}")
  private String outgoingEmail;

  // TODO replace this
  private final String host = "http://localhost:8080/";

  public void sendPasswordResetEmail(String emailAddress, String token) {
    String text = "You are receiving this email because a password reset request has been made for "
        + "your account. To reset your password, click the link below and follow the provided "
        + "instructions:\n\n" + host + contextPath + "auth/passwordreset?token="
        + URLEncoder.encode(token, StandardCharsets.UTF_8)
        + "&email=" + URLEncoder.encode(emailAddress, StandardCharsets.UTF_8);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(emailAddress);
    message.setFrom(outgoingEmail);
    message.setSubject("Study Tracker: Password reset request");
    message.setText(text);
    mailSender.send(message);
  }

  public void sendNewUserEmail(String emailAddress, String token) {
    String text = "Welcome to Study Tracker! To activate your account, click on the link below to "
        + "confirm your registration and set a new password for your account.\n\n"
        + host + contextPath + "auth/passwordreset?token="
        + URLEncoder.encode(token, StandardCharsets.UTF_8)
        + "&email=" + URLEncoder.encode(emailAddress, StandardCharsets.UTF_8);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(emailAddress);
    message.setFrom(outgoingEmail);
    message.setSubject("Welcome to Study Tracker");
    message.setText(text);
    mailSender.send(message);
  }

}
