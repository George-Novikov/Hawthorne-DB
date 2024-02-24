package com.georgen.hawthorne.repositories.definite;

import com.georgen.hawthorne.repositories.GenericRepository;
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
import com.georgen.hawthorne.tools.logging.SelfTracking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SingletonEntityRepository<T> implements GenericRepository, SelfTracking {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonEntityRepository.class);

    @Override
    public File save(StorageUnit storageUnit){
        try {
            validateType(storageUnit);

            EntityUnit entityUnit = (EntityUnit) storageUnit;
            StorageArchetype archetype = entityUnit.getArchetype();

            StorageSchema storageSchema = StorageSettings.getInstance().getStorageSchema();
            storageSchema.update(archetype);

            File file = FileFactory.getFile(archetype.getPath());
            FileManager.write(file, entityUnit.getContent());

            return file;
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public <T, I> T get(StorageArchetype archetype, I... id){
        try {
            File file = FileFactory.getFile(archetype.getPath());
            if (file == null) return null;

            String json = FileManager.read(file);
            if (json == null || json.isEmpty()) throw new HawthorneException(Message.FILE_IS_CORRUPTED);

            Class javaClass = Class.forName(archetype.getFullName());
            T object = Serializer.deserialize(json, javaClass);
            if (object == null) throw new HawthorneException(Message.ENTITY_RETRIEVAL_ERROR);

            return object;
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public <I> boolean delete(StorageArchetype archetype, I... id) {
        try {
            File file = FileFactory.getFile(archetype.getPath());
            if (file == null) throw new HawthorneException(Message.DELETE_FAIL);
            return FileManager.delete(file);
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public <T> List<T> list(StorageArchetype archetype) {
        T object = get(archetype);
        return new ArrayList<T>() {{ add(object); }};
    }

    //TODO: maybe it would be better to return the number of versions
    public <T> long count(StorageArchetype archetype){
        return FileFactory.isExistingFile(archetype.getPath()) ? 1 : 0;
    }

    private void validateType(StorageUnit storageUnit) throws HawthorneException {
        boolean isEntityType = storageUnit instanceof EntityUnit;
        if (!isEntityType) throw new HawthorneException(Message.NO_ENTITY_ANNOTATION);
    }
}
