package com.example.profile_service.mapper;

import com.example.profile_service.dto.request.SkillRequest;
import com.example.profile_service.dto.response.SkillResponse;
import com.example.profile_service.entity.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    SkillMapper INSTANCE = Mappers.getMapper(SkillMapper.class);

    Skill toSkillProfile(SkillRequest request);

    @Mapping(target = "title", constant = "Skill")
    SkillResponse toSkillProfileResponse(Skill entity);
}
