package com.example.identity_service.facade;

import com.example.identity_service.Events.NotificationEvent;
import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.UserCreationRequest;

public interface EmailFacade {
    void sendVerificationEmail(UserCreationRequest request, String urlEmailToken);
    void sendVerificationEmailUnEnable(AuthenticationRequest request, String urlEmailToken);
    void resendVerificationEmailUnEnable(String email, String token);
    NotificationEvent buildEmailNotificationUnEnable(AuthenticationRequest request, String urlEmailToken);
    NotificationEvent buildEmailNotification(UserCreationRequest request, String urlEmailToken);
    String buildVerificationEmailBody(String email, String verificationUrl);
    String generateEmailBody(UserCreationRequest request, String urlEmailToken);
    String generateEmailBodyUnEnable(AuthenticationRequest request, String urlEmailToken);
}
