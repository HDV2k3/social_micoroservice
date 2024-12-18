// package com.example.identity_service.entity;
//
// import jakarta.persistence.*;
// import lombok.*;
// import lombok.experimental.FieldDefaults;
//
// import java.util.Set;
//
// @Getter
// @Setter
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// @FieldDefaults(level = AccessLevel.PRIVATE)
// @Entity
// public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    String id;
//    String email;
//    String password;
//    @ManyToMany
//    Set<Role> roles;
// }
package com.example.identity_service.entity;

import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String email;
    String password;

    @Column(nullable = false)
    boolean enabled;

    String verificationToken;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles;
}
