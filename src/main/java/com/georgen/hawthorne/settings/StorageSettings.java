package com.georgen.hawthorne.settings;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.model.exceptions.InitializationException;
import com.georgen.hawthorne.model.messages.SystemMessage;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.serialization.StorageSchemaSerializer;
import com.georgen.hawthorne.tools.PathBuilder;
import com.georgen.hawthorne.tools.Validator;

import java.io.File;
import java.io.IOException;

import static com.georgen.hawthorne.model.constants.ConfigProperty.*;

public class StorageSettings {
    private ConfigReader configReader;
    private StorageSchema storageSchema;

    private StorageSettings(){
        this.configReader = new ConfigReader();
        File controlFile = getControlFile();

        try {
            String storageSchemaJson = FileManager.read(controlFile);
            this.storageSchema = StorageSchemaSerializer.deserialize(storageSchemaJson);
        } catch (Exception e){
            this.storageSchema = new StorageSchema(controlFile);
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

    public String getBinaryDataPath() {
        try {
            String binaryDataPath = this.configReader.getProperty(BINARY_DATA_PATH);
            boolean isValidPath = Validator.isValid(binaryDataPath);
            return getPathRelativeToRoot(isValidPath ? binaryDataPath : BINARY_DATA_PATH.getDefaultValue());
        } catch (Exception e){
            return BINARY_DATA_PATH.getDefaultValue();
        }
    }

    public File getControlFile() {
        try {
            return FileFactory.getFile(configReader.getControlFilePath());
        } catch (Exception e){
            throw new InitializationException(SystemMessage.CONTROL_FILE_LOAD_FAIL, e);
        }
    }

    public StorageSchema getStorageSchema(){
        return this.storageSchema;
    }

    public int getPartitioningThreshold(){
        try {
            String stringThresholdValue = this.configReader.getProperty(PARTITIONING_THRESHOLD);
            return stringThresholdValue != null ? Integer.valueOf(stringThresholdValue) : Integer.valueOf(PARTITIONING_THRESHOLD.getDefaultValue());
        } catch (Exception e) {

            return Integer.valueOf(PARTITIONING_THRESHOLD.getDefaultValue());
        }
    }

    private String getPathRelativeToRoot(String path){
        return PathBuilder.concat(getRootPath(), path);
    }
}
