package com.example.notification_service.controller;



import com.example.notification_service.dto.request.Recipient;
import com.example.notification_service.dto.request.SendEmailRequest;
import com.example.notification_service.event.NotificationEvent;
import com.example.notification_service.service.EmailServiceKafka;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    EmailServiceKafka emailServiceKafka;
    @KafkaListener(topics = "notification-delivery",groupId = "notification-verify-group")
    public void listenNotificationDelivery(NotificationEvent message){
        log.info("Message received: {}", message);
        emailServiceKafka.sendEmail(SendEmailRequest.builder()
                .to(Recipient.builder()
                        .email(message.getRecipient())
                        .build())
                .subject(message.getSubject())
                .htmlContent(message.getBody())
                .build());
    }
}
