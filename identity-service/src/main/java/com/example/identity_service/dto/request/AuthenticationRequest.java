package com.example.identity_service.dto.request;

import com.example.identity_service.validator.DobConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    @Size(min = 4, message = "Email_INVALID")
    @Email
    String email;
    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;
    String ipAddress;
    String userAgent;
    String firstName;
    String lastName;

}
