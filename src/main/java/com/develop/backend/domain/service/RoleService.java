package com.develop.backend.domain.service;

import com.develop.backend.application.dto.RoleDto;

import java.util.List;

public interface RoleService {
    RoleDto findById(Long id);

    RoleDto findByName(String name);

    RoleDto save(RoleDto roleDto);

    RoleDto update(RoleDto roleDto);

    List<RoleDto> findAll();

    void delete(Long id);

}
