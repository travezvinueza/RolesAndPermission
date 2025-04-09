package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.PermissionDto;
import com.develop.backend.domain.entity.Permission;
import com.develop.backend.domain.repository.PermissionRepository;
import com.develop.backend.domain.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public PermissionDto findByIdPermission(Long id) {
        return permissionRepository.findById(id)
                .map(PermissionDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    @Override
    public List<PermissionDto> findAllPermission() {
        return permissionRepository.findAll().stream()
                .map(PermissionDto::fromEntity)
                .toList();
    }

    @Override
    public PermissionDto savePermission(PermissionDto permissionDto) {
        if (permissionRepository.findByPermissionName(permissionDto.getPermissionName()).isPresent()) {
            throw new RuntimeException("Permission already exists");
        }
        Permission permission = Permission.fromDto(permissionDto);
        permission = permissionRepository.save(permission);
        return PermissionDto.fromEntity(permission);
    }

    @Override
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        permissionRepository.delete(permission);

    }

    @Override
    public PermissionDto updatePermission(Long id, PermissionDto permissionDto) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        permission.setPermissionName(permissionDto.getPermissionName());
        permission = permissionRepository.save(permission);
        return PermissionDto.fromEntity(permission);
    }
}
