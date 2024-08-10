package com.example.profile_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectRequest {
    String projectName;
    String description;
    boolean currently;
    LocalDate start;
    LocalDate end;
    String MediaLink;
    Set<String> participantIds; // List of user IDs participating in the project
}
