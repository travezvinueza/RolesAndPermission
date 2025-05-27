package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.PermissionDto;
import com.develop.backend.domain.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionControllerTest {

    @InjectMocks
    private PermissionController permissionController;

    @Mock
    private PermissionService permissionService;

    private PermissionDto getDefaultPermissionDto(Long id, String name) {
        return PermissionDto.builder().id(id).permissionName(name).build();
    }

    @Test
    void createPermission() {
        PermissionDto inputDto = PermissionDto.builder().permissionName("CREATE_USER").build();
        PermissionDto expectedDto = getDefaultPermissionDto(1L, "CREATE_USER");

        when(permissionService.savePermission(any(PermissionDto.class))).thenReturn(expectedDto);

        ResponseEntity<PermissionDto> responseEntity = permissionController.createPermission(inputDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedDto, responseEntity.getBody());
        verify(permissionService).savePermission(eq(inputDto));
    }

    @Test
    void updatePermission() {
        Long permissionId = 1L;
        PermissionDto inputDto = getDefaultPermissionDto(permissionId, "UPDATE_USER_PROFILE");
        PermissionDto expectedDto = getDefaultPermissionDto(permissionId, "UPDATE_USER_PROFILE");

        when(permissionService.updatePermission(anyLong(), any(PermissionDto.class))).thenReturn(expectedDto);

        ResponseEntity<PermissionDto> responseEntity = permissionController.updatePermission(inputDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedDto, responseEntity.getBody());
        verify(permissionService).updatePermission(eq(permissionId), eq(inputDto));
    }

    @Test
    void deletePermission() {
        Long permissionIdToDelete = 1L;
        doNothing().when(permissionService).deletePermission(anyLong());

        ResponseEntity<Void> responseEntity = permissionController.deletePermission(permissionIdToDelete);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(permissionService).deletePermission(eq(permissionIdToDelete));
    }

    @Test
    void getAllPermissions() {
        when(permissionService.findAllPermission()).thenReturn(Collections.emptyList());

        ResponseEntity<List<PermissionDto>> responseEntity = permissionController.getAllPermissions();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.emptyList(), responseEntity.getBody());
        verify(permissionService).findAllPermission();
    }

    @Test
    void getPermissionById() {
        Long permissionId = 1L;
        PermissionDto expectedPermission = getDefaultPermissionDto(permissionId, "DELETE_USER");

        when(permissionService.findByIdPermission(anyLong())).thenReturn(expectedPermission);

        ResponseEntity<PermissionDto> responseEntity = permissionController.getPermissionById(permissionId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPermission, responseEntity.getBody());
        verify(permissionService).findByIdPermission(eq(permissionId));
    }
}