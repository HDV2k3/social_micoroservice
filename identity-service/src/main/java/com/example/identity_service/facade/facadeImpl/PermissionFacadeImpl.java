package com.example.identity_service.facade.facadeImpl;

import com.example.identity_service.dto.request.PermissionRequest;
import com.example.identity_service.dto.response.PermissionResponse;
import com.example.identity_service.facade.PermissionFacade;
import com.example.identity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionFacadeImpl implements PermissionFacade {

    PermissionService permissionService;

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        return permissionService.create(request);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        return permissionService.getAll();
    }

    @Override
    public String deletePermission(String permissionId) {
        permissionService.delete(permissionId);
        return "Delete Permission Successfully";
    }
}