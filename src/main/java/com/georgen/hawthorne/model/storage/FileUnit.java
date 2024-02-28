package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.exceptions.TypeException;
import com.georgen.hawthorne.serialization.Serializer;
import com.georgen.hawthorne.tools.id.IdGenerator;
import com.georgen.hawthorne.tools.extractors.BinaryDataExtractor;

import java.lang.reflect.InvocationTargetException;

public class FileUnit<S> extends StorageUnit<byte[], S>{
    private byte[] content;

    public FileUnit(S source) throws HawthorneException, JsonProcessingException, InvocationTargetException, IllegalAccessException, TypeException {
        this(
                new StorageArchetype(source),
                source
        );
    }

    public FileUnit(StorageArchetype archetype, S source) throws JsonProcessingException, HawthorneException, InvocationTargetException, IllegalAccessException {
        this.setArchetype(archetype);
        this.setMetadata(Serializer.toJson(source));
        this.setContent(BinaryDataExtractor.extract(source));
        this.setSource(source);

        if (IdGenerator.isGenerationRequired(this)){
            IdGenerator.generateForUnit(this);
        }
    }

    @Override
    public byte[] getContent() {
        return content;
    }

    @Override
    public void setContent(byte[] content) {
        this.content = content;
    }
}
