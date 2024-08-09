package com.example.profile_service.controller;
import com.example.profile_service.dto.request.ApiResponse;
import com.example.profile_service.dto.response.AvatarProfileResponse;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.exception.AppException;
import com.example.profile_service.exception.ErrorCode;
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
    @GetMapping("/users")
    List<UserProfileResponse> getProfiles() {
        return userProfileService.getAllProfiles();
    }
    @PostMapping("/users/{userId}/avatar")
    public ResponseEntity<ApiResponse<Void>> uploadProfileImage(
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
}
