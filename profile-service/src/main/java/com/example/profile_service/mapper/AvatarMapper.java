package com.example.profile_service.mapper;

import com.example.profile_service.dto.response.AvatarProfileResponse;
import com.example.profile_service.entity.Avatar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AvatarMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "name", target = "name")
    AvatarProfileResponse toAvatarProfileResponse(Avatar avatar);
}
