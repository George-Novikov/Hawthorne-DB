package com.georgen.hawthorne.api.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.georgen.hawthorne.api.annotations.entities.MonoEntity;
import com.georgen.hawthorne.config.SettingsContainer;
import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.tools.logging.SelfTracking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class MonoEntityRepository implements GenericRepository, SelfTracking {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonoEntityRepository.class);
    private static final ObjectMapper SERIALIZER = new ObjectMapper();
    private String entityFolderName;

    public MonoEntityRepository(){
        this.entityFolderName = SettingsContainer.getInstance().getEntitiesFolderName();
    }
    public File save(Object object){
        try {
            LOGGER.info(start());

            String className = object.getClass().getSimpleName();
            String filePath = String.format("%s%s%s.json", this.entityFolderName, File.separator, className);
            LOGGER.info("File path: {}", filePath);

            String serializedObject = SERIALIZER.writeValueAsString(object);

            File file = FileFactory.getFile(filePath);
            FileManager.write(file, serializedObject);

            return file;
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
}
