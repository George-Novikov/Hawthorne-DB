package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.tools.Serializer;

import java.io.File;

public class EntityConverter {
    public static <T> T convert(File file, StorageArchetype archetype) throws Exception {
        String json = FileManager.read(file);
        if (json == null || json.isEmpty()) throw new HawthorneException(Message.FILE_IS_CORRUPTED);

        Class javaClass = Class.forName(archetype.getFullName());
        T object = Serializer.deserialize(json, javaClass);
        if (object == null) throw new HawthorneException(Message.ENTITY_RETRIEVAL_ERROR);

        return object;
    }
}
