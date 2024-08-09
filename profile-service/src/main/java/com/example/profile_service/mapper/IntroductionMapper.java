package com.example.profile_service.mapper;

import com.example.profile_service.dto.request.IntroductionRequest;
import com.example.profile_service.dto.response.IntroductionResponse;
import com.example.profile_service.entity.Introduction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IntroductionMapper {
    Introduction toProfileIntroduction(IntroductionRequest request);
    IntroductionResponse toProfileIntroductionResponse(Introduction entity);
}
