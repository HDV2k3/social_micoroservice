package com.example.profile_service.mapper;
import com.example.profile_service.dto.request.EducationRequest;
import com.example.profile_service.dto.request.ProfileCreationRequest;
import com.example.profile_service.dto.response.EducationResponse;
import com.example.profile_service.dto.response.UserProfileResponse;
import com.example.profile_service.entity.Education;
import com.example.profile_service.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface EducationMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    Education toEducationProfile(EducationRequest request);

    EducationResponse toEducationProfileResponse(Education entity);

}
