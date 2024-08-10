package com.example.profile_service.mapper;

import com.example.profile_service.dto.request.ProjectRequest;
import com.example.profile_service.dto.response.ProjectResponse;
import com.example.profile_service.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project toProfileProject(ProjectRequest request);
    ProjectResponse toProfileProjectResponse(Project entity);
}
