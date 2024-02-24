package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.serialization.Serializer;

public class EntityUnit<S> extends StorageUnit<String, S> {
    private String content;
    public EntityUnit(S source) throws HawthorneException, JsonProcessingException {
        this(
                new StorageArchetype(source),
                source
        );
    }

    public EntityUnit(StorageArchetype archetype, S source) throws JsonProcessingException {
        this.setArchetype(archetype);
        String jsonContent = Serializer.toJson(source);
        this.setMetadata(jsonContent);
        this.setContent(jsonContent);
        this.setSource(source);
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }
}
