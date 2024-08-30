package com.example.identity_service.controller;

import java.util.List;

import com.example.identity_service.facade.PermissionFacade;
import org.springframework.web.bind.annotation.*;

import com.example.identity_service.dto.ApiResponse;
import com.example.identity_service.dto.request.PermissionRequest;
import com.example.identity_service.dto.response.PermissionResponse;
import com.example.identity_service.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionFacade permissionFacade;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        var result = permissionFacade.createPermission(request);
        return ApiResponse.success(result);
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        var result = permissionFacade.getAllPermissions();
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{permission}")
    ApiResponse<String> delete(@PathVariable String permission) {
        var result = permissionFacade.deletePermission(permission);
        return ApiResponse.success(result);
    }
}
