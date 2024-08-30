package com.example.identity_service.service;

import java.util.*;
import jakarta.mail.MessagingException;
import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
public interface UserService {
    UserResponse createUser(UserCreationRequest request);
    Map<String, String> login(AuthenticationRequest request) throws MessagingException;
    String getEmailByToken(String token);
    UserResponse getMyInfo();
    Map<String, Object> verifyEmail(String token);
    String resendVerification(String token);
    UserResponse updateUser(String userId, UserUpdateRequest request);
    void deleteUser(String userId);
    List<UserResponse> getUsers();
    UserResponse getUser(String id);

}
