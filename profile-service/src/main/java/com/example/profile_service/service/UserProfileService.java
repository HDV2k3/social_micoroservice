package com.example.profile_service.service;

import com.example.profile_service.contants.URL_BUCKET_NAME;
import com.example.profile_service

.dto.request.ProfileCreationRequest;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.entity.Avatar;
import com.example.profile_service

.entity.UserProfile;
import com.example.profile_service.exception.AppException;
import com.example.profile_service.exception.ErrorCode;
import com.example.profile_service.mapper.AvatarMapper;
import com.example.profile_service

.mapper.UserProfileMapper;
import com.example.profile_service

.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    FirebaseStorageService firebaseStorageService;
    AvatarMapper avatarMapper;
    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile =
                userProfileRepository.findById(id) .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public void uploadAvatarProfile(String userId, MultipartFile file) throws IOException {
        // Find the user profile
        UserProfile userProfile = userProfileRepository
                .findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        // Upload the avatar to Firebase and get the URL
        String imageUrl = firebaseStorageService.uploadFile(URL_BUCKET_NAME.BUCKET_NAME, URL_BUCKET_NAME.AVATAR_FOLDER, file);

        // Create a new Avatar entity
        Avatar avatar = new Avatar();
        avatar.setName(file.getOriginalFilename());
        avatar.setType(file.getContentType());
        avatar.setUrl(imageUrl);

        // Associate the avatar with the user profile
        userProfile.setAvatar(avatar);

        // Save the updated user profile
        userProfileRepository.save(userProfile);

        // Use the mapper to convert the Avatar entity to AvatarProfileResponse DTO
        avatarMapper.toAvatarProfileResponse(avatar);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        return userProfiles.stream()
                .map(userProfileMapper::toUserProfileResponse)
                .collect(Collectors.toList());
    }

}
