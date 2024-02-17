package com.georgen.hawthorne.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.georgen.hawthorne.model.storage.StorageSchema;

public class StorageSchemaSerializer {
    private static final TypeReference<StorageSchema> STORAGE_SCHEMA_TYPE = new TypeReference<StorageSchema>() {};

    public static StorageSchema deserialize(String json) throws JsonProcessingException {
        return Serializer.getInstance().readValue(json, STORAGE_SCHEMA_TYPE);
    }
}
