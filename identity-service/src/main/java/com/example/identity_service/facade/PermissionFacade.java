package com.example.identity_service.facade;

import com.example.identity_service.dto.request.PermissionRequest;
import com.example.identity_service.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionFacade {
    PermissionResponse createPermission(PermissionRequest request);
    List<PermissionResponse> getAllPermissions();
    String deletePermission(String permissionId);
}
