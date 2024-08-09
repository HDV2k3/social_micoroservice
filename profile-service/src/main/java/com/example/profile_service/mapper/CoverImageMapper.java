package com.example.profile_service.mapper;


import com.example.profile_service.entity.CoverImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CoverImageMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "name", target = "name")
    void toCoverImageProfileResponse(CoverImage coverImage);
}
