package com.example.identity_service.facade;

import com.example.identity_service.dto.request.RoleRequest;
import com.example.identity_service.dto.response.RoleResponse;

import java.util.List;

public interface RoleFacade {
    RoleResponse createRole(RoleRequest request);
    List<RoleResponse> getAllRoles();
    String deleteRole(String roleId);
}