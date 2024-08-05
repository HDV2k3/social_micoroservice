package com.example.identity_service.Events;

import java.util.UUID;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.identity_service.entity.User;
import com.example.identity_service.service.EmailService;
import com.example.identity_service.service.VerificationTokenService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(user, token);
        String recipientAddress = user.getEmail();
        String confirmationUrl = "http://localhost:3000/succes-email-verification?token=" + token;
        try {
            emailService.sendVerificationEmail(recipientAddress, confirmationUrl);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
