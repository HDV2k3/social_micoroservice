package com.example.profile_service.controller;

import com.example.profile_service.dto.ApiResponse;
import com.example.profile_service.dto.PageResponse;
import com.example.profile_service.dto.request.*;
import com.example.profile_service.dto.response.*;
import com.example.profile_service.facade.UserProfileFacade;
import com.example.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileFacade userProfileFacade;
    private static final String DEFAULT_AVATAR_URL = "https://firebasestorage.googleapis.com/v0/b/chatappjava-a7ee2.appspot.com/o/avatar%2Fimages.png?alt=media&token=8fed008f-82d6-4734-a6df-f2e5b7e87b37";

    // client use
    @GetMapping("/users/{profileId}")
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String profileId) {

        var result = userProfileFacade.getProfile(profileId);
        return  ApiResponse.success(result);
    }

    @GetMapping("/users/userId/{userId}")
    ApiResponse<UserProfileResponse> getProfileByUserId(@PathVariable String userId) {
        var result = userProfileFacade.getProfileByUserId(userId);
        return  ApiResponse.success(result);
    }
    // API trả về thông tin nhiều người dùng dựa trên danh sách userIds
    @GetMapping("/users")
    ApiResponse<PageResponse<UserProfileResponse>> getProfiles
    (@RequestParam(value = "page", required = false, defaultValue = "1") int page,
     @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var result = userProfileFacade.getProfiles(page,size);
        return  ApiResponse.success(result);
    }

    @PostMapping("/users/{profileId}/avatar")
    public ApiResponse<AvatarProfileResponse> uploadAvatarProfile(
            @PathVariable String profileId,
            @RequestParam("file") MultipartFile file) throws IOException {
        var result = userProfileFacade.uploadAvatarProfile(profileId,file);
        return  ApiResponse.success(result);
    }

    @PostMapping("/users/{profileId}/coverImage")
    public ApiResponse<CoverImageResponse> uploadCoverImageProfile(
            @PathVariable String profileId,
            @RequestParam("file") MultipartFile file) throws IOException {
        var result = userProfileFacade.uploadCoverImageProfile(profileId,file);
        return  ApiResponse.success(result);
    }

    @PostMapping("/users/{profileId}/education")
    ApiResponse<EducationResponse> createEducation(
            @PathVariable String profileId,
            @RequestBody EducationRequest request) {
        var result = userProfileFacade.createEducation(request,profileId);
        return  ApiResponse.success(result);
    }

    @PostMapping("/users/{profileId}/introduction")
    ApiResponse<IntroductionResponse> createIntroduction(
            @PathVariable String profileId,
            @RequestBody IntroductionRequest request) {
        var result = userProfileFacade.createIntroduction(request,profileId);
        return  ApiResponse.success(result);
    }

    @PostMapping("users/{profileId}/project")
    ApiResponse<ProjectResponse> createProject(@PathVariable String profileId, @RequestBody ProjectRequest request) {
        var result = userProfileFacade.createProject(request,profileId);
        return  ApiResponse.success(result);
    }

    @PostMapping("/users/{profileId}/skill")
    ApiResponse<SkillResponse> createSkill(@PathVariable String profileId, @RequestBody SkillRequest request) {
        var result = userProfileFacade.createSkill(request,profileId);
        return  ApiResponse.success(result);
    }
}
