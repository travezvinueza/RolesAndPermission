package com.develop.backend.insfraestructure.util;

import com.develop.backend.application.dto.ProductDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class CacheUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Serializar cualquier objeto a JSON
    public static String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializando JSON", e);
        }
    }

    public static ProductDto deserialize(String json) {
        try {
            return objectMapper.readValue(json, ProductDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializando JSON a ProductDto", e);
        }
    }

    public static List<ProductDto> deserializeList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<ProductDto>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializando JSON a List<ProductDto>", e);
        }
    }
}
