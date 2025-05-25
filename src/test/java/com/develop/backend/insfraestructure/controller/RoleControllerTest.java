package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.RoleDto;
import com.develop.backend.domain.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    private RoleDto getDefaultRoleDto(Long id, String roleName) {
        return RoleDto.builder()
                .id(id)
                .roleName(roleName)
                .permissionId(Set.of(1L, 2L))
                .build();
    }

    @Test
    void createRole() {
        RoleDto inputDto = RoleDto.builder().roleName("ADMIN").permissionId(Set.of(1L)).build();
        RoleDto expectedDto = getDefaultRoleDto(1L, "ADMIN");

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
        RoleDto inputDto = RoleDto.builder().id(roleId).roleName("SUPER_ADMIN").permissionId(Set.of(1L, 2L, 3L)).build();
        RoleDto expectedDto = RoleDto.builder().id(roleId).roleName("SUPER_ADMIN").permissionId(Set.of(1L, 2L, 3L)).build();


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
    void getAllRoles_whenRolesExist() {
        RoleDto role1 = getDefaultRoleDto(1L, "ADMIN");
        RoleDto role2 = getDefaultRoleDto(2L, "USER");
        List<RoleDto> expectedRoles = List.of(role1, role2);

        when(roleService.findAllRole()).thenReturn(expectedRoles);

        ResponseEntity<List<RoleDto>> responseEntity = roleController.getAllRoles();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedRoles, responseEntity.getBody());
        verify(roleService).findAllRole();
    }

    @Test
    void getAllRoles_whenNoRolesExist() {
        when(roleService.findAllRole()).thenReturn(Collections.emptyList());

        ResponseEntity<List<RoleDto>> responseEntity = roleController.getAllRoles();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.emptyList(), responseEntity.getBody());
        verify(roleService).findAllRole();
    }

    @Test
    void getRoleById_whenRoleFound() {
        Long roleId = 1L;
        RoleDto expectedRole = getDefaultRoleDto(roleId, "VIEWER");

        when(roleService.findByIdRole(anyLong())).thenReturn(expectedRole);

        ResponseEntity<RoleDto> responseEntity = roleController.getRoleById(roleId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedRole, responseEntity.getBody());
        verify(roleService).findByIdRole(eq(roleId));
    }

    @Test
    void getRoleByName_whenRoleFound() {
        String roleName = "EDITOR";
        RoleDto expectedRole = getDefaultRoleDto(1L, roleName);

        when(roleService.findByRoleName(anyString())).thenReturn(expectedRole);

        ResponseEntity<RoleDto> responseEntity = roleController.getRoleByName(roleName);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedRole, responseEntity.getBody());
        verify(roleService).findByRoleName(eq(roleName));
    }
}
