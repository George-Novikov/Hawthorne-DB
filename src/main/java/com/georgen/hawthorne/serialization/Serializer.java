package com.georgen.hawthorne.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;

public class Serializer {
    private static ObjectMapper serializer;
    private static Set<TypeReference<?>> types = new HashSet();

    public static String toJson(Object object) throws JsonProcessingException {
        return getInstance().writeValueAsString(object);
    }

    public static ObjectMapper getInstance(){
        if (serializer == null){
            synchronized (Serializer.class){
                if (serializer == null){
                    serializer = new ObjectMapper();
                }
            }
        }
        return serializer;
    }
}
