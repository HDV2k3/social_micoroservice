package com.example.profile_service.facade.facadeImpl;
import com.example.profile_service.facade.UserProfileFacade;
import com.example.profile_service.dto.PageResponse;
import com.example.profile_service.dto.request.*;
import com.example.profile_service.dto.response.*;
import com.example.profile_service.mapper.*;
import com.example.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileFacadeImpl implements UserProfileFacade {

    UserProfileService userProfileService;

    @Override
    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        return userProfileService.createProfile(request);
    }

    @Override
    public UserProfileResponse getProfile(String id) {
        return userProfileService.getProfile(id);
    }

    @Override
    public UserProfileResponse getProfileByUserId(String id) {
        return userProfileService.getProfileByUserId(id);
    }

    @Override
    public PageResponse<UserProfileResponse> getProfiles(int page, int size) {
        return userProfileService.getProfiles(page, size);
    }

    @Override
    public AvatarProfileResponse uploadAvatarProfile(String profileId, MultipartFile file) throws IOException {
        return userProfileService.uploadAvatarProfile(profileId, file);
    }

    @Override
    public CoverImageResponse uploadCoverImageProfile(String profileId, MultipartFile file) throws IOException {
        return userProfileService.uploadCoverImageProfile(profileId, file);
    }

    @Override
    public EducationResponse createEducation(EducationRequest request, String profileId) {
        return userProfileService.createEducation(request, profileId);
    }

    @Override
    public IntroductionResponse createIntroduction(IntroductionRequest request, String profileId) {
        return userProfileService.createIntroduction(request, profileId);
    }

    @Override
    public SkillResponse createSkill(SkillRequest request, String profileId) {
        return userProfileService.createSkill(request, profileId);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, String profileId) {
        return userProfileService.createProject(request, profileId);
    }

    @Override
    public List<UserProfileResponse> getAllProfiles() {
        return userProfileService.getAllProfiles();
    }
}

