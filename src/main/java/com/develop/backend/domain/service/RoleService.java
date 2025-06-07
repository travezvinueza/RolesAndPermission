package com.develop.backend.domain.service;

import com.develop.backend.application.dto.RoleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    RoleDto findByIdRole(Long id);

    RoleDto saveRole(RoleDto roleDto);

    RoleDto updateRole(Long id, RoleDto roleDto);

    Page<RoleDto> findAllRole(String roleName, Pageable pageable);

    void deleteRole(Long id);
}