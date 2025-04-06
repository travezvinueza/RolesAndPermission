package com.develop.backend.application.dto;

import com.develop.backend.domain.entity.Permission;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto {
    private Long id;
    private String permissionName;


    public static PermissionDto fromEntity(Permission permission) {
        return PermissionDto.builder()
                .id(permission.getId())
                .permissionName(permission.getPermissionName())
                .build();
    }
}
