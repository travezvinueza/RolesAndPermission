package com.develop.backend.domain.service;

import com.develop.backend.application.dto.RoleDto;

import java.util.List;

public interface RoleService {
    RoleDto findByIdRole(Long id);

    RoleDto findByRoleName(String roleName);

    RoleDto saveRole(RoleDto roleDto);

    RoleDto updateRole(RoleDto roleDto);

    List<RoleDto> findAllRole();

    void deleteRole(Long id);

}
