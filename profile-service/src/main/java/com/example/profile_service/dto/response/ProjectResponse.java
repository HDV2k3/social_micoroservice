package com.example.profile_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectResponse {
    String id;
    String projectName;
    String description;
    boolean currently;
    LocalDate start;
    LocalDate end;
    String MediaLink;
    Set<UserProfileResponse> participants; // Detailed information about participants
}


