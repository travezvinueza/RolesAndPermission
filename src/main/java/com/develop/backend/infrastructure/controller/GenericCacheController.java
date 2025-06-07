package com.develop.backend.infrastructure.controller;

import com.develop.backend.domain.service.GenericCacheService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v3/cache")
@RequiredArgsConstructor
public class GenericCacheController {
    private final GenericCacheService genericCacheService;

    @PostMapping("/{key}")
    public ResponseEntity<Void> saveToCache(@PathVariable String key, @RequestBody Object value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Cache key cannot be null or empty");
        }
        if (value == null) {
            throw new IllegalArgumentException("Cache value cannot be null");
        }
        genericCacheService.saveToCache(key, value);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{key}")
    public ResponseEntity<Object> getFromCache(@PathVariable String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Cache key cannot be null or empty");
        }
        Optional<Object> cached = genericCacheService.getFromCache(key, new TypeReference<Object>() {
        });
        return cached.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> invalidateCache(@PathVariable String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Cache key cannot be null or empty");
        }
        genericCacheService.invalidateCache(key);
        return ResponseEntity.noContent().build();
    }
}
