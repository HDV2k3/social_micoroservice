package com.example.identity_service.service;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.mail.MessagingException;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.identity_service.Events.OnRegistrationCompleteEvent;
import com.example.identity_service.constant.PredefinedRole;
import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.PermissionResponse;
import com.example.identity_service.dto.response.RoleResponse;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.Role;
import com.example.identity_service.entity.User;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.exception.ErrorCode;
import com.example.identity_service.mapper.ProfileMapper;
import com.example.identity_service.mapper.UserMapper;
import com.example.identity_service.repository.RoleRepository;
import com.example.identity_service.repository.UserRepository;
import com.example.identity_service.repository.httpclient.ProflieClient;
import com.example.identity_service.validator.CustomAuthenticationToken;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    ProflieClient proflieClient;
    ProfileMapper profileMapper;
    CheckIPService checkIPService;
    ApplicationEventPublisher eventPublisher;
    VerificationTokenService validateVerificationToken;
    EmailService emailService;
    AuthenticationManager authenticationManager;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.EMAIL_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user = userRepository.save(user);
        checkIPService.addUserLocation(user, request.getIpAddress());

        log.info(request.getIpAddress());
        System.out.println("ProfileMapper: " + profileMapper);
        System.out.println("Request: " + request);
        var profileRequest = profileMapper.toProfileCreationRequest(request);
        profileRequest.setUserId(user.getId());
        var profileResponse = proflieClient.createProfile(profileRequest);

        log.info("User after save: {}", user);
        log.info("ProfileResponse: {}", profileResponse);

        User savedUser =
                userRepository.findById(user.getId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        log.info("Saved User: {}", savedUser);

        Set<RoleResponse> roleResponses = savedUser.getRoles().stream()
                .map(role -> {
                    Set<PermissionResponse> permissionResponses = role.getPermissions().stream()
                            .map(permission ->
                                    new PermissionResponse(permission.getName(), permission.getDescription()))
                            .collect(Collectors.toSet());
                    return new RoleResponse(role.getName(), role.getDescription(), permissionResponses);
                })
                .collect(Collectors.toSet());

        UserResponse userResponse = UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(profileResponse.getFirstName())
                .lastName(profileResponse.getLastName())
                .dob(profileResponse.getDob())
                .city(profileResponse.getCity())
                .roles(roleResponses)
                .build();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(savedUser));
        log.info("UserResponse: {}", userResponse);
        return userResponse;
    }

    public Map<String, String> login(AuthenticationRequest request) throws MessagingException {
        CustomAuthenticationToken authToken = new CustomAuthenticationToken(
                request.getEmail(), request.getPassword(), request.getIpAddress(), request.getUserAgent());

        authenticationManager.authenticate(authToken);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(null);
        Map<String, String> response = new HashMap<>();

        if (!user.isEnabled()) {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
            response.put("message", "Registration successful. Please check your email to verify your account.");
            response.put("verificationToken", user.getVerificationToken());
        }
        return response;
    }

    public String getEmailByToken(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);
        if (user != null) {
            return user.getEmail();
        } else {
            throw new RuntimeException("Invalid token");
        }
    }

    public Map<String, Object> verifyEmail(String token) {
        Map<String, Object> response = new HashMap<>();
        String result = validateVerificationToken.validateVerificationToken(token);

        if ("valid".equals(result)) {
            String email = getEmailByToken(token);
            if (email != null) {
                response.put("email", email);
                response.put("message", "Email verified successfully! You can now log in.");
            } else {
                response.put("message", "Invalid token.");
            }
        } else {
            response.put("message", "Invalid or expired verification token.");
        }

        return response;
    }

    public String resendVerification(String token) throws MessagingException {
        String email = getEmailByToken(token);
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "User not found";
        }

        if (user.isEnabled()) {
            return "User is already verified";
        }

        UserResponse userResponse = getMyInfo();

        if (userResponse == null) {
            return "User not found";
        }

        String confirmationUrl = "http://localhost:3000/succes-email-verification?token=" + token;
        emailService.sendVerificationEmail(email, confirmationUrl);
        return "Verification email resent";
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('USER')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('USER')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('USER')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('USER')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)));
    }
}
