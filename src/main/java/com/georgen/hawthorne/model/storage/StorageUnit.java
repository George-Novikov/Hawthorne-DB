package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.exceptions.TypeException;

import java.lang.reflect.InvocationTargetException;

public abstract class StorageUnit<C, S> {
    private StorageArchetype archetype;
    private String metadata;
    private S source;

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

    public S getSource() {
        return source;
    }

    public void setSource(S source) {
        this.source = source;
    }

    public abstract C getContent();

    public abstract void setContent(C content);

    public boolean hasEmptySource(){
        return this.source == null;
    }

    public static StorageUnit of(Object object) throws HawthorneException, JsonProcessingException, InvocationTargetException, IllegalAccessException, TypeException {
        StorageArchetype archetype = new StorageArchetype(object);
        EntityType entityType = archetype.getEntityType();

        if (entityType.isFile()){
            return new FileUnit(archetype, object);
        }

        return new EntityUnit(archetype, object);
    }
}
