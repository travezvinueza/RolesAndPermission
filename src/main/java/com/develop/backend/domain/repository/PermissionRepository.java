package com.develop.backend.domain.repository;

import com.develop.backend.domain.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionName(String permissionName);
    Page<Permission> findByPermissionNameContainingIgnoreCase(String permissionName, Pageable pageable);
}
