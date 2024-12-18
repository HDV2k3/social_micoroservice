package com.example.profile_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EducationResponse {
    String id;
    String nameSchool;
    String degree;
    LocalDate start;
    LocalDate end;
    String fieldOfStudy;
    String gpa;
    String activitiesAndSocieties;
    String description;
}
