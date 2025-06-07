package com.develop.backend.infrastructure.controller;

import com.develop.backend.domain.service.GenericCacheService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenericCacheControllerTest {

    @InjectMocks
    private GenericCacheController genericCacheController;

    @Mock
    private GenericCacheService genericCacheService;

    @Test
    void saveToCache() {
        String key = "testKey";
        String value = "testValue";

        doNothing().when(genericCacheService).saveToCache(anyString(), any());

        ResponseEntity<Void> responseEntity = genericCacheController.saveToCache(key, value);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(genericCacheService).saveToCache(eq(key), eq(value));
    }

    @Test
    void getFromCache() {
        String key = "testKey";
        String expectedObject = "cachedValue";

        when(genericCacheService.getFromCache(eq(key), any(TypeReference.class)))
                .thenReturn(Optional.of(expectedObject));

        ResponseEntity<Object> responseEntity = genericCacheController.getFromCache(key);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedObject, responseEntity.getBody());
        verify(genericCacheService).getFromCache(eq(key), any(TypeReference.class));
    }

    @Test
    void invalidateCache() {
        String key = "testKey";

        doNothing().when(genericCacheService).invalidateCache(anyString());

        ResponseEntity<Void> responseEntity = genericCacheController.invalidateCache(key);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(genericCacheService).invalidateCache(eq(key));
    }
}