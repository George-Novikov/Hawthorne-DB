package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.serialization.Serializer;
import com.georgen.hawthorne.tools.extractors.ByteContentExtractor;

import java.lang.reflect.InvocationTargetException;

public class FileUnit extends StorageUnit<byte[]>{

    public FileUnit(Object object) throws HawthorneException, JsonProcessingException, InvocationTargetException, IllegalAccessException {
        this(
                new StorageArchetype(object),
                object
        );
    }

    public FileUnit(StorageArchetype archetype, Object object) throws JsonProcessingException, HawthorneException, InvocationTargetException, IllegalAccessException {
        this.setArchetype(archetype);
        this.setMetadata(Serializer.toJson(object));
        this.setContent(ByteContentExtractor.extract(object));
    }
}
