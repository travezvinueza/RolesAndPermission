package com.develop.backend.domain.service;

import com.develop.backend.application.dto.PermissionDto;

import java.util.List;

public interface PermissionService {
    PermissionDto findByIdPermission(Long id);

    List<PermissionDto> findAllPermission();

    PermissionDto savePermission(PermissionDto permissionDto);

    void deletePermission(Long id);

    PermissionDto updatePermission(Long id, PermissionDto permissionDto);
}
