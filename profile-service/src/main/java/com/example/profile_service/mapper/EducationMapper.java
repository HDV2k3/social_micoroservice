package com.example.profile_service.mapper;
import com.example.profile_service.dto.request.EducationRequest;
import com.example.profile_service.dto.response.EducationResponse;
import com.example.profile_service.entity.Education;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface EducationMapper {
    Education toEducationProfile(EducationRequest request);
    EducationResponse toEducationProfileResponse(Education entity);
}
