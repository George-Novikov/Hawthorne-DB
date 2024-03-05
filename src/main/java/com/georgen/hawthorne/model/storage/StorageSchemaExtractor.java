package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class StorageSchemaExtractor {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final SimpleModule SIMPLE_MODULE = new SimpleModule();
    private static final StorageSchemaDeserializer DESERIALIZER = new StorageSchemaDeserializer(StorageSchema.class);

    public static StorageSchema extract(String storageSchemaJson) throws JsonProcessingException {
        SIMPLE_MODULE.addDeserializer(StorageSchema.class, DESERIALIZER);
        OBJECT_MAPPER.registerModule(SIMPLE_MODULE);
        return OBJECT_MAPPER.readValue(storageSchemaJson, StorageSchema.class);
    }
}
