package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.RoleDto;
import com.develop.backend.domain.repository.RoleRepository;
import com.develop.backend.domain.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public RoleDto findById(Long id) {
        return null;
    }

    @Override
    public RoleDto findByName(String name) {
        return null;
    }

    @Override
    public RoleDto save(RoleDto roleDto) {
        return null;
    }

    @Override
    public RoleDto update(RoleDto roleDto) {
        return null;
    }

    @Override
    public List<RoleDto> findAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
}
