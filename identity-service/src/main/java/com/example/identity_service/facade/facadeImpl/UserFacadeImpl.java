package com.example.identity_service.facade.facadeImpl;

import com.example.identity_service.dto.request.AuthenticationRequest;
import com.example.identity_service.dto.request.UserCreationRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.exception.AppException;
import com.example.identity_service.facade.UserFacade;
import com.example.identity_service.service.UserService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserFacadeImpl implements UserFacade {

    UserService userService;

    @Override
    public UserResponse createUser(UserCreationRequest request) throws AppException {
        try {
            return userService.createUser(request);
        } catch (AppException e) {
            log.error("Error creating user: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<String, String> login(AuthenticationRequest request) throws AppException, MessagingException {
        try {
            return userService.login(request);
        } catch (AppException | MessagingException e) {
            log.error("Error logging in: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public String getEmailByToken(String token) throws AppException {
        try {
            return userService.getEmailByToken(token);
        } catch (AppException e) {
            log.error("Error getting email by token: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public UserResponse getMyInfo() throws AppException {
        try {
            return userService.getMyInfo();
        } catch (AppException e) {
            log.error("Error getting user info: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<String, Object> verifyEmail(String token) throws AppException {
        try {
            return userService.verifyEmail(token);
        } catch (AppException e) {
            log.error("Error verifying email: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public String resendVerification(String token) throws AppException {
        try {
            return userService.resendVerification(token);
        } catch (AppException e) {
            log.error("Error resending verification: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public UserResponse updateUser(String userId, UserUpdateRequest request) throws AppException {
        try {
            return userService.updateUser(userId, request);
        } catch (AppException e) {
            log.error("Error updating user: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public String deleteUser(String userId) throws AppException {
        try {
            userService.deleteUser(userId);
        } catch (AppException e) {
            log.error("Error deleting user: {}", e.getMessage());
            throw e;
        }
        return "Delete User Successfully";
    }

    @Override
    public List<UserResponse> getUsers() throws AppException {
        try {
            return userService.getUsers();
        } catch (AppException e) {
            log.error("Error getting users: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public UserResponse getUser(String id) throws AppException {
        try {
            return userService.getUser(id);
        } catch (AppException e) {
            log.error("Error getting user: {}", e.getMessage());
            throw e;
        }
    }
}