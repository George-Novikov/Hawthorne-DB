package com.georgen.hawthorne.repositories.definite;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.EntityUnit;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.repositories.GenericRepository;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.tools.IdGenerator;
import com.georgen.hawthorne.tools.logging.SelfTracking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class EntityCollectionRepository implements GenericRepository, SelfTracking {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityCollectionRepository.class);

    @Override
    public File save(StorageUnit storageUnit){
        try {
            validateType(storageUnit);

            EntityUnit entityUnit = (EntityUnit) storageUnit;
            IdGenerator.generateForUnit(entityUnit);

            StorageArchetype archetype = entityUnit.getArchetype();
            StorageSchema storageSchema = StorageSettings.getInstance().getStorageSchema();
            storageSchema.update(archetype);

            //TODO: concat archetype root path with the file id
            String path = archetype.getPath();

            File file = FileFactory.getFile(path);
            FileManager.write(file, entityUnit.getContent());

            return file;
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public <T, I> T get(StorageArchetype archetype, I... id){
        return null;
    }

    @Override
    public <I> boolean delete(StorageArchetype archetype, I... id) {
        return false;
    }

    @Override
    public <T> List<T> list(StorageArchetype archetype) {
        return null;
    }

    @Override
    public <T> long count(StorageArchetype archetype) {
        return 0;
    }

    private void validateType(StorageUnit storageUnit) throws HawthorneException {
        boolean isEntityType = storageUnit instanceof EntityUnit;
        if (!isEntityType) throw new HawthorneException(Message.NO_ENTITY_ANNOTATION);
    }
}