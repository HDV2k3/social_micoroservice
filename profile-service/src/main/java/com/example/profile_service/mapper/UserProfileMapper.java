package com.example.profile_service.mapper;

import com.example.profile_service

.dto.request.ProfileCreationRequest;
import com.example.profile_service

.dto.response.UserProfileReponse;
import com.example.profile_service

.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileReponse toUserProfileReponse(UserProfile entity);
}
