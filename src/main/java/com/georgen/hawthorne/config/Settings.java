package com.georgen.hawthorne.config;

import java.io.File;

import static com.georgen.hawthorne.model.constants.ConfigProperty.ENTITIES_DIRECTORY_NAME;
import static com.georgen.hawthorne.model.constants.ConfigProperty.ROOT_DIRECTORY_NAME;

public class Settings {
    private SystemConfig config;
    private String rootFolderName;
    private String entitiesFolderName;
    private String filesFolderName;
    private String collectionsFolderName;

    public Settings(){
        this.config = new SystemConfig();
    }

    public SystemConfig getConfig() {
        return config;
    }

    public void setConfig(SystemConfig config) {
        this.config = config;
    }

    public String getRootFolderName() {
        try {
            return this.rootFolderName != null ? this.rootFolderName : this.config.getProperty(ROOT_DIRECTORY_NAME);
        } catch (Exception e){
            return ROOT_DIRECTORY_NAME.getDefaultValue();
        }
    }

    public void setRootFolderName(String rootFolderName) {
        this.rootFolderName = rootFolderName;
    }

    public String getEntitiesFolderName() {
        try {
            String entitiesFolderName = this.entitiesFolderName != null ? this.entitiesFolderName : this.config.getProperty(ENTITIES_DIRECTORY_NAME);
            return String.format(
                    "%s%s%s",
                    this.getRootFolderName(),
                    File.separator,
                    entitiesFolderName
            );
        } catch (Exception e){
            return ENTITIES_DIRECTORY_NAME.getDefaultValue();
        }
    }

    public void setEntitiesFolderName(String entitiesFolderName) {
        this.entitiesFolderName = entitiesFolderName;
    }

    public String getFilesFolderName() {
        return filesFolderName;
    }

    public void setFilesFolderName(String filesFolderName) {
        this.filesFolderName = filesFolderName;
    }

    public String getCollectionsFolderName() {
        return collectionsFolderName;
    }

    public void setCollectionsFolderName(String collectionsFolderName) {
        this.collectionsFolderName = collectionsFolderName;
    }
}
