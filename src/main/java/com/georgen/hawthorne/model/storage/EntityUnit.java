package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.hawthorne.model.exceptions.FileException;
import com.georgen.hawthorne.serialization.Serializer;

public class EntityUnit extends StorageUnit<String> {

    public EntityUnit(Object object) throws FileException, JsonProcessingException {
        this(
                new StorageArchetype(object),
                object
        );
    }

    public EntityUnit(StorageArchetype archetype, Object object) throws JsonProcessingException {
        this.setArchetype(archetype);
        String jsonContent = Serializer.toJson(object);
        this.setMetadata(jsonContent);
        this.setContent(jsonContent);
    }
}
