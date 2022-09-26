package com.exchanges.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestUtil {
    private static final ObjectMapper objectMapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    static final String PATH = "src/test/resources/com/exchanges";


    public static File readFile(String filePath, String fileName) {
        return Paths.get(PATH, filePath, fileName).toAbsolutePath().toFile();
    }

    public static <T> T fromJson(String filePath, String fileName, Class<T> tClass) {
        try {
            String json = objectMapper.readTree(readFile(filePath, fileName)).toString();

            return JsonUtils.fromJson(json, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String filePath, String fileName, TypeReference<T> typeReference) {
        try {
            String json = objectMapper.readTree(readFile(filePath, fileName)).toString();

            return JsonUtils.fromJson(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readJson(String filePath, String fileName) {
        try {
            return objectMapper
                    .readTree(readFile(filePath, fileName))
                    .toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
