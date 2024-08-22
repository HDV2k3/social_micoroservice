package com.example.profile_service.controller;

import com.example.profile_service.dto.PageResponse;
import com.example.profile_service.dto.request.*;
import com.example.profile_service.dto.response.*;
import com.example.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;
    private static final String DEFAULT_AVATAR_URL = "https://firebasestorage.googleapis.com/v0/b/chatappjava-a7ee2.appspot.com/o/avatar%2Fimages.png?alt=media&token=8fed008f-82d6-4734-a6df-f2e5b7e87b37";

    // client use
    @GetMapping("/users/{profileId}")
    UserProfileResponse getProfile(@PathVariable String profileId) {
        return userProfileService.getProfile(profileId);
    }

    // API trả về thông tin nhiều người dùng dựa trên danh sách userIds
    @GetMapping("/users")
    ApiResponse<PageResponse<UserProfileResponse>> getProfiles
    (@RequestParam(value = "page", required = false, defaultValue = "1") int page,
     @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ApiResponse.<PageResponse<UserProfileResponse>>builder()
                .result(userProfileService.getProfiles(page, size))
                .build();
    }

    @PostMapping("/users/{profileId}/avatar")
    public ResponseEntity<ApiResponse<Void>> uploadAvatarProfile(
            @PathVariable String userId,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }
            userProfileService.uploadAvatarProfile(userId, file);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .message("Profile image uploaded successfully")
                    .code(200)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile image: " + e.getMessage());
        }
    }

    @PostMapping("/users/{profileId}/coverImage")
    public ResponseEntity<ApiResponse<Void>> uploadCoverImageProfile(
            @PathVariable String profileId,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }
            userProfileService.uploadCoverImageProfile(profileId, file);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .message("Cover image uploaded successfully")
                    .code(200)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload cover image: " + e.getMessage());
        }
    }

    @PostMapping("/users/{profileId}/education")
    ApiResponse<EducationResponse> createEducation(
            @PathVariable String profileId,
            @RequestBody EducationRequest request) {
        return ApiResponse.<EducationResponse>builder().
                result(userProfileService.createEducation(request, profileId)).
                build();
    }

    @PostMapping("/users/{profileId}/introduction")
    ApiResponse<IntroductionResponse> createIntroduction(
            @PathVariable String profileId,
            @RequestBody IntroductionRequest request) {
        return ApiResponse.<IntroductionResponse>builder().
                result(userProfileService.createIntroduction(request, profileId)).
                build();
    }

    @PostMapping("users/{profileId}/project")
    ApiResponse<ProjectResponse> createProject(@PathVariable String profileId, @RequestBody ProjectRequest request) {
        return ApiResponse.<ProjectResponse>builder()
                .result(userProfileService.createProject(request, profileId))
                .build();
    }

    @PostMapping("/users/{profileId}/skill")
    ApiResponse<SkillResponse> createSkill(@PathVariable String profileId, @RequestBody SkillRequest request) {
        return ApiResponse.<SkillResponse>builder()
                .result(userProfileService.createSkill(request, profileId))
                .build();
    }
}
