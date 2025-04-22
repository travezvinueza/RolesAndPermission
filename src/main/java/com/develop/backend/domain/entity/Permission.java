package com.develop.backend.domain.entity;

import com.develop.backend.application.dto.PermissionDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "permission_name", nullable = false, unique = true)
    private String permissionName;

    @Override
    public String getAuthority() {
        return permissionName;
    }


    public static Permission fromDto(PermissionDto permissionDto) {
        return Permission.builder()
                .id(permissionDto.getId())
                .permissionName(permissionDto.getPermissionName())
                .build();
    }
}
