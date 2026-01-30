package com.teamflow.teamflow.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOnboardingEmail(
            String to,
            String tempPassword
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("teamflowapp17@gmail.com");
        message.setTo(to);
        message.setSubject("Welcome to TeamFlow 🚀");

        message.setText(
                "Hello,\n\n" +
                        "Your TeamFlow account has been created.\n\n" +
                        "Login details:\n" +
                        "Email: " + to + "\n" +
                        "Temporary Password: " + tempPassword + "\n\n" +
                        "Please login and change your password immediately.\n\n" +
                        "App link: http://localhost:3000/login\n\n" +
                        "— TeamFlow"
        );

        mailSender.send(message);
    }
}
