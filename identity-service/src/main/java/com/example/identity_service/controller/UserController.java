package com.example.identity_service.controller;

import java.util.List;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.identity_service.dto.request.ApiResponse;
import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.service.UserService;
import com.example.identity_service.service.VerificationTokenService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;
    @PostMapping("/register")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@RequestBody AuthenticationRequest request) {
        try {
            Map<String, String> response = userService.login(request);
            return ApiResponse.<Map<String, String>>builder().result(response).build();
        } catch (MessagingException e) {
            return ApiResponse.<Map<String, String>>builder().build();
        }
    }

    @PostMapping("/verify-email")
    public ApiResponse<Map<String, Object>> verifyEmail(@RequestParam("token") String token) {
        Map<String, Object> response = userService.verifyEmail(token);
        if (response.containsKey("email")) {
            return ApiResponse.<Map<String, Object>>builder().result(response).build();
        } else {
            return ApiResponse.<Map<String, Object>>builder().build();
        }
    }

    @PostMapping("/resend-verification")
    public ApiResponse<String> resendVerification(@RequestParam("token") String token) {
        String message = userService.resendVerification(token);
        return ApiResponse.<String>builder().result(message).build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
}
