package com.georgen.hawthorne.settings;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileIOManager;
import com.georgen.hawthorne.model.exceptions.InitializationException;
import com.georgen.hawthorne.model.messages.SystemMessage;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.model.storage.StorageSchemaExtractor;
import com.georgen.hawthorne.tools.paths.PathBuilder;
import com.georgen.hawthorne.tools.Validator;

import java.io.File;

import static com.georgen.hawthorne.model.constants.ConfigProperty.*;

public class StorageSettings {

    private ConfigReader configReader;
    private StorageSchema storageSchema;

    private StorageSettings(){
        this.configReader = new ConfigReader();
        File controlFile = getControlFile();

        try {
            String storageSchemaJson = FileIOManager.read(controlFile);
            this.storageSchema = StorageSchemaExtractor.extract(storageSchemaJson);
            this.storageSchema.setControlFile(controlFile);
        } catch (Exception e){
            this.storageSchema = new StorageSchema();
            this.storageSchema.setControlFile(controlFile);
        }
    }

    private static class StorageSettingsInitializer {
        private static final StorageSettings INSTANCE = new StorageSettings();
    }

    public static StorageSettings getInstance(){
        return StorageSettingsInitializer.INSTANCE;
    }

    public String getRootPath() {
        try {
            String rootFolderName = this.configReader.getProperty(ROOT_PATH);
            return Validator.isValid(rootFolderName) ? rootFolderName : ROOT_PATH.getDefaultValue();
        } catch (Exception e){
            return ROOT_PATH.getDefaultValue();
        }
    }

    public String getEntitiesPath() {
        try {
            String entitiesPath = this.configReader.getProperty(ENTITIES_PATH);
            boolean isValidPath = Validator.isValid(entitiesPath);
            return getPathRelativeToRoot(isValidPath ? entitiesPath : ENTITIES_PATH.getDefaultValue());
        } catch (Exception e){
            return ENTITIES_PATH.getDefaultValue();
        }
    }

    public File getControlFile() {
        try {
            return FileFactory.getInstance().getFile(configReader.getControlFilePath(), true);
        } catch (Exception e){
            throw new InitializationException(SystemMessage.CONTROL_FILE_LOAD_FAIL, e);
        }
    }

    public StorageSchema getStorageSchema(){
        return this.storageSchema;
    }


    private String getPathRelativeToRoot(String path){
        return PathBuilder.concatenate(getRootPath(), path);
    }
}
