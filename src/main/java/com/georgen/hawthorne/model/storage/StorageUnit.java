package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.exceptions.HawthorneException;

import java.lang.reflect.InvocationTargetException;

public abstract class StorageUnit<T> {
    private StorageArchetype archetype;
    private String metadata;
    private T content;

    public StorageArchetype getArchetype() {
        return archetype;
    }

    public void setArchetype(StorageArchetype archetype) {
        this.archetype = archetype;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public static StorageUnit of(Object object) throws HawthorneException, JsonProcessingException, InvocationTargetException, IllegalAccessException {
        StorageArchetype archetype = new StorageArchetype(object);
        EntityType entityType = archetype.getEntityType();

        if (entityType.isFile()){
            return new FileUnit(archetype, object);
        }

        return new EntityUnit(archetype, object);
    }
}
