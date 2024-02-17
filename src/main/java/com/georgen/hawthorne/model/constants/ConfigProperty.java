package com.georgen.hawthorne.model.constants;

public enum ConfigProperty {
    CONTROL_FILE_NAME("hawthorne.naming.control-file", "storage.json"),
    ROOT_PATH("hawthorne.naming.root-directory", "hawthorne"),
    STORAGE_PATH("hawthorne.naming.storage-directory", "storage"),
    ENTITIES_PATH("hawthorne.naming.entities-directory", "entities"),
    COLLECTIONS_PATH("hawthorne.naming.collections-directory", "collections"),
    FILES_PATH("hawthorne.naming.files-directory", "files")
    ;

    private String name;
    private String defaultValue;

    ConfigProperty(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName(){
        return this.name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
