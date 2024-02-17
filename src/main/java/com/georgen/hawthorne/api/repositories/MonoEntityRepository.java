package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.config.Settings;
import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.serialization.Serializer;
import com.georgen.hawthorne.tools.logging.SelfTracking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class MonoEntityRepository<T> implements GenericRepository, SelfTracking {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonoEntityRepository.class);
    private String entityFolderName;

    public MonoEntityRepository(){
        this.entityFolderName = Settings.getInstance().getEntitiesPath();
    }
    @Override
    public File save(Object object){
        try {
            LOGGER.info(start());

            StorageUnit unit = new StorageUnit(object);
            StorageSchema storageSchema = Settings.getInstance().getStorageSchema();
            storageSchema.register(unit);
            storageSchema.save();

            LOGGER.info("File path: {}", unit.getPath());

            String serializedObject = Serializer.toJson(object);

            File file = FileFactory.getFile(unit.getPath());
            FileManager.write(file, serializedObject);

            return file;
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

//    public T get(Class javaClass){
//        try {
//            String className = javaClass.getSimpleName();
//            EntityType entityType = EntityType.of(javaClass);
//        } catch (FileException e) {
//            LOGGER.error(e.getMessage(), e);
//            return null;
//        }
//    }
}
