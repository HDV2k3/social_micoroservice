package com.example.identity_service.controller;

import java.util.List;

import com.example.identity_service.facade.RoleFacade;
import org.springframework.web.bind.annotation.*;

import com.example.identity_service.dto.ApiResponse;
import com.example.identity_service.dto.request.RoleRequest;
import com.example.identity_service.dto.response.RoleResponse;
import com.example.identity_service.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleFacade roleFacade;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
       var result = roleFacade.createRole(request);
       return ApiResponse.success(result);
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        var result = roleFacade.getAllRoles();
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{role}")
    ApiResponse<String> delete(@PathVariable String role) {
        var result = roleFacade.deleteRole(role);
        return ApiResponse.success(result);
    }
}
