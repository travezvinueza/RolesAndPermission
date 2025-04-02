package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.RoleDto;
import com.develop.backend.domain.entity.Role;
import com.develop.backend.domain.repository.RoleRepository;
import com.develop.backend.domain.service.RoleService;
import com.develop.backend.insfraestructure.exception.RoleNotFoundException;
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
        Role existingRole = roleRepository.findByRoleName(roleDto.getRoleName()).orElse(null);
        if (existingRole != null) {
            throw new RoleNotFoundException("El rol ya existe");
        }

        Role role = new Role();
        role.setRoleName(roleDto.getRoleName());
        roleRepository.save(role);

        return RoleDto.builder()
                .id(role.getId())
                .roleName(roleDto.getRoleName())
                .build();
    }

    @Override
    public RoleDto update(RoleDto roleDto) {
        Role existingRole = roleRepository.findById(roleDto.getId())
                .orElseThrow(() -> new RoleNotFoundException("Rol no encontrado"));

        existingRole.setRoleName(roleDto.getRoleName());
        roleRepository.save(existingRole);

        return RoleDto.builder()
                .id(existingRole.getId())
                .roleName(existingRole.getRoleName())
                .build();

    }

    @Override
    public List<RoleDto> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(role -> RoleDto.builder()
                        .id(role.getId())
                        .roleName(role.getRoleName())
                        .build())
                .toList();
    }

    @Override
    public void delete(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(null);
        roleRepository.delete(role);
    }
}
