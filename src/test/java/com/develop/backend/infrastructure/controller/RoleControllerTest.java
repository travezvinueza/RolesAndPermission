package com.develop.backend.infrastructure.controller;

import com.develop.backend.application.dto.RoleDto;
import com.develop.backend.domain.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    private RoleDto getDefaultRoleDto() {
        return RoleDto.builder().id(1L).roleName("ROLE_ADMIN").permissionId(Set.of(1L, 2L)).build();
    }

    private RoleDto getDefaultRoleDtoWithPermissions() {
        return RoleDto.builder().id(1L).roleName("ROLE_USER").permissionId(Set.of(1L, 2L, 3L)).build();
    }

    @Test
    void createRole() {
        RoleDto inputDto = RoleDto.builder().roleName("ROLE_ADMIN").permissionId(Set.of(1L)).build();
        RoleDto expectedDto = getDefaultRoleDto();

        when(roleService.saveRole(any(RoleDto.class))).thenReturn(expectedDto);

        ResponseEntity<RoleDto> responseEntity = roleController.createRole(inputDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedDto, responseEntity.getBody());
        verify(roleService).saveRole(eq(inputDto));
    }

    @Test
    void updateRole() {
        Long roleId = 1L;
        RoleDto inputDto = RoleDto.builder().id(roleId).roleName("ROLE_ADMIN").permissionId(Set.of(1L, 2L, 3L)).build();
        RoleDto expectedDto = RoleDto.builder().id(roleId).roleName("ROLE_ADMIN").permissionId(Set.of(1L, 2L, 3L)).build();


        when(roleService.updateRole(anyLong(), any(RoleDto.class))).thenReturn(expectedDto);

        ResponseEntity<RoleDto> responseEntity = roleController.updateRole(inputDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedDto, responseEntity.getBody());
        verify(roleService).updateRole(eq(roleId), eq(inputDto));
    }

    @Test
    void deleteRole() {
        Long roleIdToDelete = 1L;
        doNothing().when(roleService).deleteRole(anyLong());

        ResponseEntity<Void> responseEntity = roleController.deleteRole(roleIdToDelete);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(roleService).deleteRole(eq(roleIdToDelete));
    }

    @Test
    void getAllRoles() {
        RoleDto role1 = getDefaultRoleDto();
        RoleDto role2 = getDefaultRoleDtoWithPermissions();

        List<RoleDto> expectedRoles = List.of(role1, role2);
        Page<RoleDto> roleDtoPage = new PageImpl<>(expectedRoles);

        when(roleService.findAllRole(eq(null), any(Pageable.class))).thenReturn(roleDtoPage);

        ResponseEntity<Page<RoleDto>> responseEntity = roleController.getAllRoles(null, 0, 10);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().getContent().size());
        assertEquals(role1, responseEntity.getBody().getContent().get(0));
        assertEquals(role2, responseEntity.getBody().getContent().get(1));
        verify(roleService).findAllRole(eq(null), any(Pageable.class));
    }

    @Test
    void getRoleById() {
        Long roleId = 1L;
        RoleDto expectedRole = getDefaultRoleDto();

        when(roleService.findByIdRole(anyLong())).thenReturn(expectedRole);

        ResponseEntity<RoleDto> responseEntity = roleController.getRoleById(roleId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedRole, responseEntity.getBody());
        verify(roleService).findByIdRole(eq(roleId));
    }

}