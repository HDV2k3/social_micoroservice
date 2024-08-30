package com.example.identity_service.facade.facadeImpl;
import com.example.identity_service.Events.NotificationEvent;
import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.facade.EmailFacade;
import com.example.identity_service.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailFacadeImpl  implements EmailFacade {

    EmailService emailService;

    @Override
    public void sendVerificationEmail(UserCreationRequest request, String urlEmailToken) {
        emailService.sendVerificationEmail(request, urlEmailToken);
    }

    @Override
    public void sendVerificationEmailUnEnable(AuthenticationRequest request, String urlEmailToken) {
        emailService.sendVerificationEmailUnEnable(request, urlEmailToken);
    }

    @Override
    public void resendVerificationEmailUnEnable(String email, String token) {
        emailService.resendVerificationEmailUnEnable(email, token);
    }

    @Override
    public NotificationEvent buildEmailNotificationUnEnable(AuthenticationRequest request, String urlEmailToken) {
        return emailService.buildEmailNotificationUnEnable(request, urlEmailToken);
    }

    @Override
    public NotificationEvent buildEmailNotification(UserCreationRequest request, String urlEmailToken) {
        return emailService.buildEmailNotification(request, urlEmailToken);
    }

    @Override
    public String buildVerificationEmailBody(String email, String verificationUrl) {
        return emailService.buildVerificationEmailBody(email, verificationUrl);
    }

    @Override
    public String generateEmailBody(UserCreationRequest request, String urlEmailToken) {
        return emailService.generateEmailBody(request, urlEmailToken);
    }

    @Override
    public String generateEmailBodyUnEnable(AuthenticationRequest request, String urlEmailToken) {
        return emailService.generateEmailBodyUnEnable(request, urlEmailToken);
    }
}
