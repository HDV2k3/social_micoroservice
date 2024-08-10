package com.example.profile_service.mapper;


import com.example.profile_service.dto.response.CoverImageResponse;
import com.example.profile_service.entity.CoverImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CoverImageMapper {

    CoverImageResponse toCoverImageProfileResponse(CoverImage coverImage);
}
