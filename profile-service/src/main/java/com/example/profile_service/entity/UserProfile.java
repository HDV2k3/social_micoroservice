package com.example.profile_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Node("user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;
    @Property("userId")
    String userId;
    String firstName;
    String lastName;
    LocalDate dob;
    String city;
    String address;
    String number;
    Instant createAt;
    @Relationship(type = "HAS_AVATAR")
    Avatar avatar;

    @Relationship(type = "HAS_COVER_IMAGE")
    CoverImage coverImage;

    @Relationship(type = "HAS_EDUCATION")
    Education education;

    @Relationship(type = "HAS_INTRODUCTION")
    Introduction introduction;

    @Relationship(type = "HAS_SKILL")
    Set<Skill> skills = new HashSet<>();
    // Relationship to the projects this user is involved in
    @Relationship(type = "INVOLVES", direction = Relationship.Direction.INCOMING)
    Set<Project> projects = new HashSet<>();
    // has_.. profile nay co...
    //INVOLVES profile nay bao gom voi user...
}
