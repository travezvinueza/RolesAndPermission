package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.PermissionDto;
import com.develop.backend.domain.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v3/permission")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("/create")
    public ResponseEntity<PermissionDto> createPermission(@RequestBody PermissionDto permissionDto) {
        PermissionDto createdPermission = permissionService.savePermission(permissionDto);
        return ResponseEntity.status(201).body(createdPermission);
    }

    @PostMapping("/update")
    public ResponseEntity<PermissionDto> updatePermission(@RequestBody PermissionDto permissionDto) {
        PermissionDto updatedPermission = permissionService.updatePermission(permissionDto.getId(), permissionDto);
        return ResponseEntity.ok(updatedPermission);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        List<PermissionDto> permissions = permissionService.findAllPermission();
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDto> getPermissionById(@PathVariable Long id) {
        PermissionDto permission = permissionService.findByIdPermission(id);
        return ResponseEntity.ok(permission);
    }

}
