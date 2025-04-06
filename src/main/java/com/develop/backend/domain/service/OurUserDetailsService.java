package com.develop.backend.domain.service;

import com.develop.backend.domain.entity.Permission;
import com.develop.backend.domain.entity.User;
import com.develop.backend.domain.entity.Role;
import com.develop.backend.domain.repository.PermissionRepository;
import com.develop.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OurUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PermissionService permissionService;
    private final PermissionRepository permissionRepository;
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Cargar el usuario con roles y permisos
        User user = userRepository.findByUsername(identifier)
                .orElseGet(() -> userRepository.findByEmail(identifier)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + identifier)));

        boolean hasAllPermissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> permission.getPermissionName().equals("ALL_PERMISSIONS"));

        if (hasAllPermissions) {
            List<Permission> allPermissions = permissionRepository.findAll();

            // NO guardes estas entidades después, solo úsalas para construir el UserDetails
            for (Role role : user.getRoles()) {
                role.setPermissions(new HashSet<>(allPermissions));
            }
}
        return user;
    }
}
