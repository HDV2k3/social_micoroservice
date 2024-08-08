package com.example.notification_service.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class NotificationEvent {
    String channel;
    String recipient;
    String templateCode;
    Map<String,Object> param;
    String subject;
    String body;
}