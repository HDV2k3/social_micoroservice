package com.example.profile_service.mapper;

import com.example.profile_service

.dto.request.ProfileCreationRequest;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.entity.Avatar;
import com.example.profile_service

.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileResponse(UserProfile entity);

    default String avatarToString(Avatar avatar) {
        return avatar != null ? avatar.getUrl() : null;
    }
}
