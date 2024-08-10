package com.example.profile_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Node("project")
public class Project {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;
    String projectName;
    String description;
    boolean currently;
    LocalDate start;
    LocalDate end;
    String MediaLink;
    // Relationship to other UserProfiles involved in the project
    @Relationship(type = "INVOLVES")
    Set<UserProfile> participants = new HashSet<>();
}
