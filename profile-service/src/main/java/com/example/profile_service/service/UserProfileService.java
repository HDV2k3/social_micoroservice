package com.example.profile_service.service;

import com.example.profile_service.contants.URL_BUCKET_NAME;
import com.example.profile_service.dto.request.EducationRequest;
import com.example.profile_service.dto.request.IntroductionRequest;
import com.example.profile_service

.dto.request.ProfileCreationRequest;
import com.example.profile_service.dto.response.EducationResponse;
import com.example.profile_service.dto.response.IntroductionResponse;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.entity.*;
import com.example.profile_service.exception.AppException;
import com.example.profile_service.exception.ErrorCode;
import com.example.profile_service.mapper.*;
import com.example.profile_service.repository.EducationRepository;
import com.example.profile_service.repository.IntroductionRepository;
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
    EducationRepository educationRepository;
    UserProfileMapper userProfileMapper;
    FirebaseStorageService firebaseStorageService;
    AvatarMapper avatarMapper;
    CoverImageMapper coverImageMapper;
    EducationMapper educationMapper;
    IntroductionMapper introductionMapper;
    IntroductionRepository introductionRepository;
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
    public void uploadCoverImageProfile(String profileId, MultipartFile file) throws IOException {
        UserProfile userProfile = userProfileRepository
                .findById(profileId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        String imageUrl = firebaseStorageService.uploadFile(URL_BUCKET_NAME.BUCKET_NAME, URL_BUCKET_NAME.COVER_IMAGE_FOLDER, file);

        CoverImage coverImage =new CoverImage();
        coverImage.setName(file.getOriginalFilename());
        coverImage.setType(file.getContentType());
        coverImage.setUrl(imageUrl);

        userProfile.setCoverImage(coverImage);

        userProfileRepository.save(userProfile);

        coverImageMapper.toCoverImageProfileResponse(coverImage);
    }
    public EducationResponse createEducation(EducationRequest request,String profileId)
    {
                userProfileRepository
                .findById(profileId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        Education education = educationMapper.toEducationProfile(request);
        education = educationRepository.save(education);
        return educationMapper.toEducationProfileResponse(education);
    }

    public IntroductionResponse createIntroduction(IntroductionRequest request,String profileId)
    {
        userProfileRepository
                .findById(profileId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));
        Introduction introduction = introductionMapper.toProfileIntroduction(request);
        introduction = introductionRepository.save(introduction);
        return  introductionMapper.toProfileIntroductionResponse(introduction);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        return userProfiles.stream()
                .map(userProfileMapper::toUserProfileResponse)
                .collect(Collectors.toList());
    }

}
