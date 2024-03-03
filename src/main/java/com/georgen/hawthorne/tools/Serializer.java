package com.georgen.hawthorne.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;

public class Serializer {

    public static String toJson(Object object) throws JsonProcessingException {
        return getInstance().writeValueAsString(object);
    }

    public static <T> T deserialize(String json, Class javaClass) throws JsonProcessingException {
        return (T) getInstance().readValue(json, javaClass);
    }

    private static class SerializerHolder {
        private static final ObjectMapper INSTANCE = new ObjectMapper();
    }

    public static ObjectMapper getInstance(){
        return SerializerHolder.INSTANCE;
    }
}
