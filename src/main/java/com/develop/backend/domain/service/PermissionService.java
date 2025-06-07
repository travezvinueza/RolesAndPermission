package com.develop.backend.domain.service;

import com.develop.backend.application.dto.PermissionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PermissionService {
    PermissionDto findByIdPermission(Long id);

    Page<PermissionDto> findAllPermission(String permissionName, Pageable pageable);

    PermissionDto savePermission(PermissionDto permissionDto);

    void deletePermission(Long id);

    PermissionDto updatePermission(Long id, PermissionDto permissionDto);
}
