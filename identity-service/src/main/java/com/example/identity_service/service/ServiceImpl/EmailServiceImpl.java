package com.example.identity_service.service.ServiceImpl;

import com.example.identity_service.Events.NotificationEvent;
import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.service.EmailService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {
    KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendVerificationEmail(UserCreationRequest request, String urlEmailToken) {
        NotificationEvent notificationEvent = buildEmailNotification(request, urlEmailToken);
        kafkaTemplate.send("notification-delivery", notificationEvent);
    }

    @Override
    public void sendVerificationEmailUnEnable(AuthenticationRequest request, String urlEmailToken) {
        NotificationEvent notificationEvent = buildEmailNotificationUnEnable(request, urlEmailToken);
        kafkaTemplate.send("notification-delivery", notificationEvent);
    }

    @Override
    public void resendVerificationEmailUnEnable(String email, String token) {
        String verificationUrl = "http://localhost:3000/success-email-verification?token=" + token;
        String body = buildVerificationEmailBody(email, verificationUrl);

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .chanel("EMAIL")
                .recipient(email)
                .subject("Resend Email Verification")
                .body(body)
                .build();
        kafkaTemplate.send("notification-delivery", notificationEvent);
    }

    @Override
    public NotificationEvent buildEmailNotificationUnEnable(AuthenticationRequest request, String urlEmailToken) {
        String emailBody = generateEmailBodyUnEnable(request, urlEmailToken);
        return NotificationEvent.builder()
                .chanel("EMAIL")
                .recipient(request.getEmail())
                .subject("Email Verification Reactivate")
                .body(emailBody)
                .build();
    }

    @Override
    public NotificationEvent buildEmailNotification(UserCreationRequest request, String urlEmailToken) {
        String emailBody = generateEmailBody(request, urlEmailToken);
        return NotificationEvent.builder()
                .chanel("EMAIL")
                .recipient(request.getEmail())
                .subject("Email Verification")
                .body(emailBody)
                .build();
    }

    @Override
    public String buildVerificationEmailBody(String email, String verificationUrl) {
        return "<!DOC TYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Email Verification</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
                + ".container { width: 100%; padding: 20px; background-color: #f4f4f4; }"
                + ".email-content { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
                + ".header { text-align: center; background-color: #007bff; color: #ffffff; padding: 10px 0; border-radius: 5px 5px 0 0; }"
                + ".header h1 { margin: 0; font-size: 24px; }"
                + ".body { padding: 20px; text-align: center; }"
                + ".body p { font-size: 16px; color: #333333; line-height: 1.5; }"
                + ".body a { display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #007bff; color: #ffffff; text-decoration: none; border-radius: 5px; }"
                + ".footer { text-align: center; margin-top: 20px; color: #777777; font-size: 12px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"email-content\">"
                + "<div class=\"header\">"
                + "<h1>Email Verification</h1>"
                + "</div>"
                + "<div class=\"body\">"
                + "<p>Hello " + email + " </p>"
                + "<p>Thank you for registering with us. Please click the button below to verify your email address:</p>"
                + "<a href=\"" + verificationUrl + "\" target=\"_blank\">Verify Email</a>"
                + "<p>If you did not create an account, no further action is required.</p>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>&copy; 2024 MyApp. All rights reserved.</p>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    @Override
    public String generateEmailBody(UserCreationRequest request, String urlEmailToken) {
        return "<!DOC TYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Email Verification</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
                + ".container { width: 100%; padding: 20px; background-color: #f4f4f4; }"
                + ".email-content { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
                + ".header { text-align: center; background-color: #007bff; color: #ffffff; padding: 10px 0; border-radius: 5px 5px 0 0; }"
                + ".header h1 { margin: 0; font-size: 24px; }"
                + ".body { padding: 20px; text-align: center; }"
                + ".body p { font-size: 16px; color: #333333; line-height: 1.5; }"
                + ".body a { display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #007bff; color: #ffffff; text-decoration: none; border-radius: 5px; }"
                + ".footer { text-align: center; margin-top: 20px; color: #777777; font-size: 12px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"email-content\">"
                + "<div class=\"header\">"
                + "<h1>Email Verification</h1>"
                + "</div>"
                + "<div class=\"body\">"
                + "<p>Hello " + request.getFirstName() + " " + request.getLastName() + "</p>"
                + "<p>Thank you for registering with us. Please click the button below to verify your email address:</p>"
                + "<a href=\"" + urlEmailToken + "\" target=\"_blank\">Verify Email</a>"
                + "<p>If you did not create an account, no further action is required.</p>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>&copy; 2024 MyApp. All rights reserved.</p>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    @Override
    public String generateEmailBodyUnEnable(AuthenticationRequest request, String urlEmailToken) {
        return "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Email Verification</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }"
                + ".container { width: 100%; padding: 20px; background-color: #f4f4f4; }"
                + ".email-content { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
                + ".header { text-align: center; background-color: #007bff; color: #ffffff; padding: 10px 0; border-radius: 5px 5px 0 0; }"
                + ".header h1 { margin: 0; font-size: 24px; }"
                + ".body { padding: 20px; text-align: center; }"
                + ".body p { font-size: 16px; color: #333333; line-height: 1.5; }"
                + ".body a { display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #007bff; color: #ffffff; text-decoration: none; border-radius: 5px; }"
                + ".footer { text-align: center; margin-top: 20px; color: #777777; font-size: 12px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"email-content\">"
                + "<div class=\"header\">"
                + "<h1>Email Verification</h1>"
                + "</div>"
                + "<div class=\"body\">"
                + "<p>Hello " + request.getEmail() + "</p>"
                + "<p>Please click the button below to verify your email address:</p>"
                + "<a href=\"" + urlEmailToken + "\" target=\"_blank\">Verify Email</a>"
                + "<p>If you did not create an account, no further action is required.</p>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>&copy; 2024 MyApp. All rights reserved.</p>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}
