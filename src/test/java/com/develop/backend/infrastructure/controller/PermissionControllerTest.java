package com.develop.backend.infrastructure.controller;

import com.develop.backend.application.dto.PermissionDto;
import com.develop.backend.domain.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        PermissionDto permissionDto1 = PermissionDto.builder()
                .id(1L)
                .permissionName("DELETE_USER")
                .build();

        PermissionDto permissionDto2 = PermissionDto.builder()
                .id(2L)
                .permissionName("VIEW_USER")
                .build();

        List<PermissionDto> expectedPermissions = List.of(permissionDto1, permissionDto2);
        Page<PermissionDto> permissionDtoPage = new PageImpl<>(expectedPermissions);

        when(permissionService.findAllPermission(eq(null), any(Pageable.class))).thenReturn(permissionDtoPage);

        ResponseEntity<Page<PermissionDto>> responseEntity = permissionController.getAllPermissions(null, 0, 10);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().getContent().size());
        assertEquals(expectedPermissions, responseEntity.getBody().getContent());
        assertEquals(permissionDto1, responseEntity.getBody().getContent().get(0));
        assertEquals(permissionDto2, responseEntity.getBody().getContent().get(1));

        // Verifica que el método fue llamado con los valores esperados
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(permissionService).findAllPermission(eq(null), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(0, capturedPageable.getPageNumber());
        assertEquals(10, capturedPageable.getPageSize());
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