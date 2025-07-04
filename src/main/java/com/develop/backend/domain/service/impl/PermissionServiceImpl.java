package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.PermissionDto;
import com.develop.backend.domain.entity.Permission;
import com.develop.backend.domain.repository.PermissionRepository;
import com.develop.backend.domain.service.PermissionService;
import com.develop.backend.infrastructure.exception.PermissionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public PermissionDto findByIdPermission(Long id) {
        return permissionRepository.findById(id)
                .map(PermissionDto::fromEntity)
                .orElseThrow(() -> new PermissionNotFoundException("Permission not found"));
    }

    @Override
    public Page<PermissionDto> findAllPermission(String permissionName, Pageable pageable) {
        Page<Permission> permissionPage;

        if (permissionName != null && !permissionName.isBlank()) {
            permissionPage = permissionRepository.findByPermissionNameContainingIgnoreCase(permissionName, pageable);
        } else {
            permissionPage = permissionRepository.findAll(pageable);
        }
        return permissionPage.map(PermissionDto::fromEntity);
    }

    @Override
    public PermissionDto savePermission(PermissionDto permissionDto) {
        if (permissionRepository.findByPermissionName(permissionDto.getPermissionName()).isPresent()) {
            throw new PermissionNotFoundException("Permission already exists");
        }
        Permission permission = Permission.fromDto(permissionDto);
        permission = permissionRepository.save(permission);
        return PermissionDto.fromEntity(permission);
    }

    @Override
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionNotFoundException("Permission not found for delete"));
        permissionRepository.delete(permission);
    }

    @Override
    public PermissionDto updatePermission(Long id, PermissionDto permissionDto) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new PermissionNotFoundException("Permission not found for update"));
        permission.setPermissionName(permissionDto.getPermissionName());
        permission = permissionRepository.save(permission);
        return PermissionDto.fromEntity(permission);
    }
}
