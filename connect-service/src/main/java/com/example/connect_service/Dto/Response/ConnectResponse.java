package com.example.connect_service.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConnectResponse {
    String id;

    String followerId;

    String followingId;

    Instant followedAt;

    String status;

    Instant acceptedAt;
}
