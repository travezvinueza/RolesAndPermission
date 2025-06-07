package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.RoleDto;
import com.develop.backend.domain.entity.Permission;
import com.develop.backend.domain.entity.Role;
import com.develop.backend.domain.repository.PermissionRepository;
import com.develop.backend.domain.repository.RoleRepository;
import com.develop.backend.domain.service.RoleService;
import com.develop.backend.infrastructure.exception.PermissionNotFoundException;
import com.develop.backend.infrastructure.exception.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public RoleDto findByIdRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        return RoleDto.fromEntity(role);
    }

    @Override
    public Page<RoleDto> findAllRole(String roleName, Pageable pageable) {
        Page<Role> rolePage;

        if (roleName != null && !roleName.isBlank()) {
            rolePage = roleRepository.findByRoleNameContaining(roleName, pageable);
        } else {
            rolePage = roleRepository.findAll(pageable);
        }
        return rolePage.map(RoleDto::fromEntity);
    }

    @Override
    public RoleDto saveRole(RoleDto roleDto) {
        if (roleRepository.findByRoleName(roleDto.getRoleName()).isPresent()) {
            throw new RoleNotFoundException("Role already exists");
        }

        Set<Permission> permissions = Optional.ofNullable(roleDto.getPermissionId())
                .orElse(Collections.emptySet())
                .stream()
                .map(id -> permissionRepository.findById(id)
                        .orElseThrow(() -> new PermissionNotFoundException("Permission not found: " + id)))
                .collect(Collectors.toSet());

        Role role = Role.fromDto(roleDto, permissions);

        var roleSave = roleRepository.save(role);

        return RoleDto.fromEntity(roleSave);
    }

    @Override
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found for update"));

        Set<Permission> permissions = Optional.ofNullable(roleDto.getPermissionId())
                .orElse(Collections.emptySet())
                .stream()
                .map(permissionId -> permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new PermissionNotFoundException("Permission not found: " + permissionId)))
                .collect(Collectors.toSet());

        existingRole.setRoleName(roleDto.getRoleName());
        existingRole.setPermissions(permissions);

        Role updatedRole = roleRepository.save(existingRole);

        return RoleDto.fromEntity(updatedRole);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found for delete"));
        roleRepository.delete(role);
    }
}