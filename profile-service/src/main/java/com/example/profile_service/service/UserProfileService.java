package com.example.profile_service.service;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.profile_service

.dto.request.ProfileCreationRequest;
import com.example.profile_service

.dto.response.UserProfileReponse;
import com.example.profile_service

.entity.UserProfile;
import com.example.profile_service.exception.AppException;
import com.example.profile_service.exception.ErrorCode;
import com.example.profile_service

.mapper.UserProfileMapper;
import com.example.profile_service

.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileReponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileReponse(userProfile);
    }

    public UserProfileReponse getProfile(String id) {
        UserProfile userProfile =
                userProfileRepository.findById(id) .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        return userProfileMapper.toUserProfileReponse(userProfile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileReponse> getAllProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        return userProfiles.stream()
                .map(userProfileMapper::toUserProfileReponse)
                .collect(Collectors.toList());
    }

}
