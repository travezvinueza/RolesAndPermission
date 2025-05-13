package com.develop.backend.domain.service;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Optional;

public interface GenericCacheService {
    <T> void saveToCache(String key, T object);
    void invalidateCache(String key);
    <T> Optional<T> getFromCache(String key, Class<T> clazz);
    <T> Optional<T> getFromCache(String key, TypeReference<T> typeReference);
    <T> List<T> getListFromCache(String key, TypeReference<List<T>> typeReference);
}

