package com.example.identity_service.facade.facadeImpl;

import com.example.identity_service.dto.request.RoleRequest;
import com.example.identity_service.dto.response.RoleResponse;
import com.example.identity_service.facade.RoleFacade;
import com.example.identity_service.service.RoleService;
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
public class RoleFacadeImpl implements RoleFacade {

    RoleService roleService;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        return roleService.create(request);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleService.getAll();
    }

    @Override
    public String deleteRole(String roleId) {
        roleService.delete(roleId);
        return "Delete Role Successfully";
    }
}
