package com.example.identity_service.facade;
import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.exception.AppException;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Map;

public interface UserFacade {
    UserResponse createUser(UserCreationRequest request) throws AppException;
    Map<String, String> login(AuthenticationRequest request) throws AppException, MessagingException;
    String getEmailByToken(String token) throws AppException;
    UserResponse getMyInfo() throws AppException;
    Map<String, Object> verifyEmail(String token) throws AppException;
    String resendVerification(String token) throws AppException;
    UserResponse updateUser(String userId, UserUpdateRequest request) throws AppException;
    String deleteUser(String userId) throws AppException;
    List<UserResponse> getUsers() throws AppException;
    UserResponse getUser(String id) throws AppException;
}