package com.example.profile_service.facade;


import com.example.profile_service.dto.PageResponse;
import com.example.profile_service.dto.request.*;
import com.example.profile_service.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserProfileFacade {

    UserProfileResponse createProfile(ProfileCreationRequest request);

    UserProfileResponse getProfile(String id);

    UserProfileResponse getProfileByUserId(String id);

    PageResponse<UserProfileResponse> getProfiles(int page, int size);

    AvatarProfileResponse uploadAvatarProfile(String profileId, MultipartFile file) throws IOException;

    CoverImageResponse uploadCoverImageProfile(String profileId, MultipartFile file) throws IOException;

    EducationResponse createEducation(EducationRequest request, String profileId);

    IntroductionResponse createIntroduction(IntroductionRequest request, String profileId);

    SkillResponse createSkill(SkillRequest request, String profileId);

    ProjectResponse createProject(ProjectRequest request, String profileId);

    List<UserProfileResponse> getAllProfiles();
}
