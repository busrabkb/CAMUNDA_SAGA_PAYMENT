package com.demo.ordersaga.camunda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

/** Converts task response DTOs to and from their JSON representation. */
@Component
public class TaskResponseJsonMapper {

    private final ObjectMapper objectMapper;

    public TaskResponseJsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String toJson(Object response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize task response", exception);
        }
    }

    public <T> T fromJson(String json, Class<T> responseType) {
        try {
            return objectMapper.readValue(json, responseType);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not deserialize task response", exception);
        }
    }
}
