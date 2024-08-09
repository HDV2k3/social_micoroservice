package com.example.notification_service.service;

import com.example.notification_service.dto.request.EmailRequest;
import com.example.notification_service.dto.request.SendEmailRequest;
import com.example.notification_service.dto.request.Sender;
import com.example.notification_service.dto.response.EmailResponse;
import com.example.notification_service.exception.AppException;
import com.example.notification_service.exception.ErrorCode;
import com.example.notification_service.repository.httpclient.EmailClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceKafka {
    private final EmailClient emailClient;
    private String apiKeyEmail;

    @Value("${api.key.email}")
    public void setApiKeyEmail(String apiKeyEmail) {
        this.apiKeyEmail = apiKeyEmail;
    }

    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("DevHuynh")
                        .email("dacviethuynh@gmail.com").build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            return emailClient.sendEmail(apiKeyEmail, emailRequest);
        } catch (Exception e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}