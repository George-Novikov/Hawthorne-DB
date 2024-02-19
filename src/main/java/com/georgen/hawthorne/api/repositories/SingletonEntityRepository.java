package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.serialization.Serializer;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.FileMessage;
import com.georgen.hawthorne.model.storage.EntityUnit;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.tools.logging.SelfTracking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SingletonEntityRepository<T> implements GenericRepository, SelfTracking {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonEntityRepository.class);
    private String entityFolderName;

    public SingletonEntityRepository(){
        this.entityFolderName = StorageSettings.getInstance().getEntitiesPath();
    }
    @Override
    public File save(StorageUnit storageUnit){
        try {
            LOGGER.info(start());
            validateType(storageUnit);

            EntityUnit entityUnit = (EntityUnit) storageUnit;
            StorageArchetype archetype = entityUnit.getArchetype();

            StorageSchema storageSchema = StorageSettings.getInstance().getStorageSchema();
            storageSchema.update(archetype);
            storageSchema.save();

            LOGGER.info("File path: {}", archetype.getPath());

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
            String json = FileManager.read(file);

            Class javaClass = Class.forName(archetype.getFullName());
            T object = Serializer.deserialize(json, javaClass);
            if (object == null) throw new HawthorneException(FileMessage.ENTITY_RETRIEVAL_ERROR);

            return object;
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private void validateType(StorageUnit storageUnit) throws HawthorneException {
        boolean isEntityType = storageUnit instanceof EntityUnit;
        if (!isEntityType) throw new HawthorneException(FileMessage.NO_ENTITY_ANNOTATION);
    }
}
