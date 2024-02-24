package com.georgen.hawthorne.repositories.handlers;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.EntityUnit;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.serialization.Serializer;
import com.georgen.hawthorne.settings.StorageSettings;

import java.io.File;
import java.io.IOException;

public class EntityUnitManager extends StorageUnitManager {
    @Override
    public File save(StorageUnit storageUnit) throws Exception {
        validateType(storageUnit);

        EntityUnit entityUnit = (EntityUnit) storageUnit;
        StorageArchetype archetype = entityUnit.getArchetype();

        StorageSchema storageSchema = StorageSettings.getInstance().getStorageSchema();
        storageSchema.update(archetype);

        File file = FileFactory.getFile(archetype.getPath());
        FileManager.write(file, entityUnit.getContent());

        return file;
    }

    @Override
    public <T, I> T get(StorageArchetype archetype, I... id) throws Exception {
        File file = FileFactory.getFile(archetype.getPath());
        if (file == null) return null;

        String json = FileManager.read(file);
        if (json == null || json.isEmpty()) throw new HawthorneException(Message.FILE_IS_CORRUPTED);

        Class javaClass = Class.forName(archetype.getFullName());
        T object = Serializer.deserialize(json, javaClass);
        if (object == null) throw new HawthorneException(Message.ENTITY_RETRIEVAL_ERROR);

        return object;
    }

    @Override
    public <I> boolean delete(StorageArchetype archetype, I... id) throws HawthorneException, IOException {
        File file = FileFactory.getFile(archetype.getPath());
        if (file == null) throw new HawthorneException(Message.DELETE_FAIL);
        return FileManager.delete(file);
    }

    private void validateType(StorageUnit storageUnit) throws HawthorneException {
        boolean isEntityType = storageUnit instanceof EntityUnit;
        if (!isEntityType) throw new HawthorneException(Message.NO_ENTITY_ANNOTATION);
    }
}
