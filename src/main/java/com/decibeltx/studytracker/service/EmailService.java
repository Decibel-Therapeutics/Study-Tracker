package com.decibeltx.studytracker.service;

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

  public void sendPasswordResetEmail(String emailAddress, String token) {
    String text = "You are receiving this email because a password reset request has been made for "
        + "your account. To reset your password, click the link below and follow the provided "
        + "instructions:\n\n" + contextPath + "/auth/passwordreset?token=" + token
        + "&email=" + emailAddress;

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(emailAddress);
    message.setFrom(outgoingEmail);
    message.setSubject("Study Tracker: Password reset request");
    message.setText(text);
    mailSender.send(message);
  }

}
