package com.develop.backend.infrastructure.controller;

import com.develop.backend.application.dto.PermissionDto;
import com.develop.backend.domain.service.PermissionService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
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

    @PutMapping("/update")
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
    public ResponseEntity<Page<PermissionDto>> getAllPermissions(@RequestParam(required = false) String permissionName,
                                                                 @RequestParam(defaultValue = "0") @Min(0) int page,
                                                                 @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PermissionDto> permissions = permissionService.findAllPermission(permissionName, pageable);
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDto> getPermissionById(@PathVariable Long id) {
        PermissionDto permission = permissionService.findByIdPermission(id);
        return ResponseEntity.ok(permission);
    }

}
