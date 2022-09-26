package com.exchanges.util;

import com.exchanges.exception.ExchangeRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonUtils {
    private static final ObjectMapper objectMapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Not successful create JSON, message problem: {}", e.getMessage(), e);

            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String str, Class<T> clazz) {
        try {
            return objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.error("Bad JSON, message problem: {}", e.getMessage(), e);

            throw new ExchangeRuntimeException("Bad JSON");
        }
    }

    public static <T> T fromJson(String str, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(str, typeReference);
        } catch (IOException e) {
            log.error("Bad JSON, message problem: {}", e.getMessage(), e);

            throw new ExchangeRuntimeException("Bad JSON");
        }
    }
}
