package com.develop.backend.application.dto;

import com.develop.backend.domain.entity.Permission;
import com.develop.backend.domain.entity.Role;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private Long id;
    private String roleName;
    private Set<Long> permissionId;


    public static RoleDto fromEntity(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .permissionId(role.getPermissions().stream()
                        .map(Permission::getId)
                        .collect(Collectors.toSet()))
                .build();
    }
}
