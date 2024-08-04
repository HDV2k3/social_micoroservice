package com.example.identity_service.service;

import com.example.identity_service

.constant.PredefinedRole;
import com.example.identity_service

.dto.request.UserCreationRequest;
import com.example.identity_service

.dto.request.UserUpdateRequest;
import com.example.identity_service

.dto.response.UserResponse;
import com.example.identity_service

.entity.Role;
import com.example.identity_service

.entity.User;
import com.example.identity_service

.exception.AppException;
import com.example.identity_service

.exception.ErrorCode;
import com.example.identity_service

.mapper.ProfileMapper;
import com.example.identity_service

.mapper.UserMapper;
import com.example.identity_service

.repository.RoleRepository;
import com.example.identity_service

.repository.UserRepository;
import com.example.identity_service

.repository.httpclient.ProflieClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

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
        var profileResponse =   proflieClient.createProfile(profileRequest);
        log.info(profileResponse.toString());


        return userMapper.toUserResponse(userRepository.save(user));
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
