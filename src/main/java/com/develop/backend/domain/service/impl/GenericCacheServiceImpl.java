package com.develop.backend.domain.service.impl;

import com.develop.backend.domain.entity.Cache;
import com.develop.backend.domain.repository.CacheRepository;
import com.develop.backend.domain.service.GenericCacheService;
import com.develop.backend.insfraestructure.util.CacheUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenericCacheServiceImpl implements GenericCacheService {

    private final CacheRepository cacheRepository;

    @Override
    public <T> void saveToCache(String key, T object) {
        String json = CacheUtils.serialize(object);
        Cache cache = Cache.builder()
                .key(key)
                .value(json)
                .build();
        cacheRepository.save(cache);
    }

    @Override
    public void invalidateCache(String key) {
        cacheRepository.deleteById(key);
    }

    @Override
    public <T> Optional<T> getFromCache(String key, Class<T> clazz) {
        return cacheRepository.findById(key)
                .map(cache -> CacheUtils.deserialize(cache.getValue(), clazz));
    }

    @Override
    public <T> Optional<T> getFromCache(String key, TypeReference<T> typeReference) {
        return cacheRepository.findById(key)
                .map(cache -> CacheUtils.deserialize(cache.getValue(), typeReference));
    }

    @Override
    public <T> List<T> getListFromCache(String key, TypeReference<List<T>> typeReference) {
        return cacheRepository.findById(key)
                .map(cache -> CacheUtils.deserialize(cache.getValue(), typeReference))
                .orElse(null);
    }

    @Scheduled(fixedDelay = 3600000)
    public void cleanOldCache() {
        Timestamp oneHourAgo = Timestamp.from(Instant.now().minus(1, ChronoUnit.HOURS));
        cacheRepository.deleteOlderThan(oneHourAgo);
        log.info("Limpiando caché expirada...");
    }
}

