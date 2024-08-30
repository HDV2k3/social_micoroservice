package com.example.identity_service.service.ServiceImpl;

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
import com.example.identity_service.repository.httpclient.ProfileClient;
import com.example.identity_service.service.EmailService;
import com.example.identity_service.service.IPCheckService;
import com.example.identity_service.service.UserService;
import com.example.identity_service.service.VerificationTokenService;
import com.example.identity_service.validator.CustomAuthenticationToken;
import jakarta.mail.MessagingException;

import java.util.*;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    ProfileMapper profileMapper;
    IPCheckService ipCheckService;
    VerificationTokenService validateVerificationToken;
    EmailService emailService;
    AuthenticationManager authenticationManager;
    VerificationTokenService tokenService;

    // Map role to RoleResponse and return UserResponse
    private UserResponse apiUserResponse(User user) {
        User savedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Set<RoleResponse> roleResponses = savedUser.getRoles().stream()
                .map(this::mapToRoleUserResponse)
                .collect(Collectors.toSet());

        UserResponse userResponse = UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .roles(roleResponses)
                .build();

        log.info("UserResponse: {}", userResponse);
        return userResponse;
    }

    // Map role to RoleResponse
    private RoleResponse mapToRoleUserResponse(Role role) {
        Set<PermissionResponse> permissionResponses = role.getPermissions().stream()
                .map(permission -> new PermissionResponse(permission.getName(), permission.getDescription()))
                .collect(Collectors.toSet());

        return new RoleResponse(role.getName(), role.getDescription(), permissionResponses);
    }

    private void validateUserEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
    }

    private void createProfileForUser(User user, UserCreationRequest request) {
        var profileRequest = profileMapper.toProfileCreationRequest(request);
        profileRequest.setUserId(user.getId());
        profileClient.createProfile(profileRequest);
    }

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        validateUserEmail(request.getEmail());

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);

        String token = tokenService.createVerificationToken(user);
        user = userRepository.save(user);

        ipCheckService.addUserLocation(user, request.getIpAddress());
        createProfileForUser(user, request);

        try {
            emailService.sendVerificationEmail(request, token);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", request.getEmail(), e.getMessage());
            userRepository.delete(user);
            throw new AppException(ErrorCode.SEND_EMAIL_ERROR);
        }

        return apiUserResponse(user);
    }

    @Override
    public Map<String, String> login(AuthenticationRequest request) throws MessagingException {
        CustomAuthenticationToken authToken = new CustomAuthenticationToken(
                request.getEmail(),
                request.getPassword(),
                request.getIpAddress(),
                request.getUserAgent()
        );

        authenticationManager.authenticate(authToken);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Map<String, String> response = new HashMap<>();

        if (!user.isEnabled()) {
            String token = tokenService.createVerificationToken(user);
            String confirmationUrl = "http://localhost:3000/success-email-verification?token=" + token;
            emailService.sendVerificationEmailUnEnable(request, confirmationUrl);
            response.put("message", "Registration successful. Please check your email to verify your account.");
            response.put("verificationToken", user.getVerificationToken());
        }
        return response;
    }

    @Override
    public String getEmailByToken(String token) {
        User user = userRepository.findByVerificationToken(token).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getEmail();
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
    public Map<String, Object> verifyEmail(String token) {
        Map<String, Object> response = new HashMap<>();
        String result = String.valueOf(validateVerificationToken.validateVerificationToken(token));
        String email = getEmailByToken(token);
        if ("valid".equals(result)) {
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

    @Override
    public String resendVerification(String token) {
        String email = getEmailByToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.isEnabled()) {
            return "User is already verified";
        }
        emailService.resendVerificationEmailUnEnable(email, token);
        return "Verification email resent";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)));
    }
}
