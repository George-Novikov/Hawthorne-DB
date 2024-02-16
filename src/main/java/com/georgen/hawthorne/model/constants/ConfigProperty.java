package com.georgen.hawthorne.model.constants;

public enum ConfigProperty {
    CONTROL_FILE_NAME("hawthorne.naming.control-file", "storage.json"),
    PATHS_FILE_NAME("hawthorne.naming.paths-file", "paths"),
    SETTINGS_FILE_NAME("hawthorne.naming.settings-file", "settings.json"),
    ROOT_DIRECTORY_NAME("hawthorne.naming.root-directory", "hawthorne"),
    STORAGE_DIRECTORY_NAME("hawthorne.naming.storage-directory", "storage"),
    ENTITIES_DIRECTORY_NAME("hawthorne.naming.entities-directory", "entities")
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
