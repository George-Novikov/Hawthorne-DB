package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.exceptions.TypeException;
import com.georgen.hawthorne.serialization.Serializer;
import com.georgen.hawthorne.tools.id.IdGenerator;

import java.io.IOException;

public class EntityUnit<S> extends StorageUnit<String, S> {
    private String content;
    public EntityUnit(S source) throws Exception {
        this(
                new StorageArchetype(source),
                source
        );
    }

    public EntityUnit(StorageArchetype archetype, S source) throws Exception {
        this.setArchetype(archetype);
        String jsonContent = Serializer.toJson(source);
        this.setMetadata(jsonContent);
        this.setContent(jsonContent);
        this.setSource(source);

        if (IdGenerator.isGenerationRequired(this)){
            Object generatedId = IdGenerator.generateForUnit(this);
            this.setGeneratedId(generatedId);
        }
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
