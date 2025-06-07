package com.develop.backend.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class CacheUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private CacheUtils() {
    }

    // Serializar cualquier objeto a JSON
    public static <T> String serialize(T object) {
        if (object == null) return null;
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializando objeto a JSON", e);
        }
    }

    // Deserializar un JSON a un objeto específico
    public static <T> T deserialize(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error deserializando JSON a objeto", e);
        }
    }

    public static <T> T deserialize(String json, TypeReference<T> typeRef) {
        if (json == null || json.isEmpty()) return null;
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error deserializando JSON con TypeReference", e);
        }
    }

    public static <T> List<T> deserializeList(String json, TypeReference<List<T>> typeRef) {
        return deserialize(json, typeRef);
    }

    public static Map<String, Object> deserializeMap(String json) {
        return deserialize(json, new TypeReference<Map<String, Object>>() {
        });
    }

    public static Map<String, List<Object>> deserializeMapList(String json) {
        return deserialize(json, new TypeReference<Map<String, List<Object>>>() {
        });
    }

    public static Map<String, Map<String, Object>> deserializeNestedMap(String json) {
        return deserialize(json, new TypeReference<Map<String, Map<String, Object>>>() {
        });
    }
}
