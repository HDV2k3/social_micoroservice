package com.example.profile_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDate;

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

    @Relationship(type = "HAS_AVATAR")
    Avatar avatar;

    @Relationship(type = "HAS_COVER_IMAGE")
    CoverImage coverImage;

    @Relationship(type = "HAS_EDUCATION")
    Education education;

    @Relationship(type = "HAS_INTRODUCTION")
    Introduction introduction;
}
