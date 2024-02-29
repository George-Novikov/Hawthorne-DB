package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.exceptions.TypeException;
import com.georgen.hawthorne.serialization.Serializer;
import com.georgen.hawthorne.tools.id.IdGenerator;
import com.georgen.hawthorne.tools.extractors.BinaryDataExtractor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FileUnit<S> extends StorageUnit<byte[], S>{
    private byte[] content;

    public FileUnit(S source) throws Exception {
        this(
                new StorageArchetype(source),
                source
        );
    }

    public FileUnit(StorageArchetype archetype, S source) throws Exception {
        this.setArchetype(archetype);
        this.setMetadata(Serializer.toJson(source));
        this.setContent(BinaryDataExtractor.extract(source));
        this.setSource(source);

        if (IdGenerator.isGenerationRequired(this)){
            Object generatedId = IdGenerator.generateForUnit(this);
            this.setGeneratedId(generatedId);
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
