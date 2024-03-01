package com.georgen.hawthorne.repositories;

import com.georgen.hawthorne.io.FileOperation;
import com.georgen.hawthorne.serialization.Serializer;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.EntityUnit;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.tools.PathBuilder;
import com.georgen.hawthorne.tools.logging.SelfTracking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SingletonEntityRepository implements GenericRepository, SelfTracking {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonEntityRepository.class);

    protected SingletonEntityRepository(){}

    @Override
    public <C, S> S save(StorageUnit<C, S> storageUnit) throws Exception {
        validateType(storageUnit);
        EntityUnit entityUnit = (EntityUnit) storageUnit;

        StorageArchetype archetype = entityUnit.getArchetype();
        StorageSchema storageSchema = StorageSettings.getInstance().getStorageSchema();
        storageSchema.update(archetype);

        String path = PathBuilder.buildEntityPath(archetype, storageUnit.isNew(), storageUnit.getGeneratedId());

        try (FileOperation fileOperation = new FileOperation(path)){
            File file = fileOperation.getFile();
            FileManager.write(file, entityUnit.getContent());
        }

        return storageUnit.getSource();
    }

    @Override
    public <T, I> T get(StorageArchetype archetype, I... id) throws Exception {
        File file = FileFactory.getFile(PathBuilder.buildEntityPath(archetype));
        if (file == null) return null;

        String json = FileManager.read(file);
        if (json == null || json.isEmpty()) throw new HawthorneException(Message.FILE_IS_CORRUPTED);

        Class javaClass = Class.forName(archetype.getFullName());
        T object = Serializer.deserialize(json, javaClass);
        if (object == null) throw new HawthorneException(Message.ENTITY_RETRIEVAL_ERROR);

        return object;
    }

    @Override
    public <I> boolean delete(StorageArchetype archetype, I... id) throws Exception {
        File file = FileFactory.getFile(PathBuilder.buildEntityPath(archetype));
        if (file == null) throw new HawthorneException(Message.DELETE_FAIL);
        return FileFactory.delete(file);
    }

    @Override
    public <T> List<T> list(StorageArchetype archetype) throws Exception {
        T object = get(archetype);
        return new ArrayList<T>() {{ add(object); }};
    }

    //TODO: maybe it would be better to return the number of versions
    public long count(StorageArchetype archetype) throws Exception {
        return FileFactory.isExistingFile(PathBuilder.buildEntityPath(archetype)) ? 1 : 0;
    }

    private void validateType(StorageUnit storageUnit) throws HawthorneException {
        boolean isEntityType = storageUnit instanceof EntityUnit;
        if (!isEntityType) throw new HawthorneException(Message.NOT_AN_ENTITY);
    }
}
