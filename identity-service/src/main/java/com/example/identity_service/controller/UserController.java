package com.example.identity_service.controller;

import java.util.List;
import java.util.Map;

import com.example.identity_service.facade.UserFacade;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.identity_service.dto.ApiResponse;
import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.service.UserService;

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
    UserFacade userFacade;
    @PostMapping("/register")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
     var result = userFacade.createUser(request);
     return ApiResponse.success(result);
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@RequestBody AuthenticationRequest request) throws MessagingException {
        var result = userFacade.login(request);
        return ApiResponse.success(result);
    }

    @PostMapping("/verify-email")
    public ApiResponse<Map<String, Object>> verifyEmail(@RequestParam("token") String token) {
        var result = userFacade.verifyEmail(token);
        return ApiResponse.success(result);
    }

    @PostMapping("/resend-verification")
    public ApiResponse<String> resendVerification(@RequestParam("token") String token) {
        var result = userFacade.resendVerification(token);
        return ApiResponse.success(result);
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var result = userFacade.getUsers();
        return ApiResponse.success(result);
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        var result = userFacade.getUser(userId);
        return ApiResponse.success(result);
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        var result = userFacade.getMyInfo();
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        var result = userFacade.deleteUser(userId);
        return ApiResponse.success(result);
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        var result = userFacade.updateUser(userId,request);
        return ApiResponse.success(result);
    }
}
