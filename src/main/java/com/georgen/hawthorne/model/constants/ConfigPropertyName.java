package com.georgen.hawthorne.model.constants;

public enum ConfigPropertyName {
    CONTROL_FILE_NAME("hawthorne.naming.control-file"),
    PATHS_FILE_NAME("hawthorne.naming.paths-file"),
    SETTINGS_FILE_NAME("hawthorne.naming.settings-file"),
    ROOT_DIRECTORY_NAME("hawthorne.naming.root-directory"),
    STORAGE_DIRECTORY_NAME("hawthorne.naming.storage-directory"),
    ENTITIES_DIRECTORY_NAME("hawthorne.naming.entities-directory")
    ;

    private String name;

    ConfigPropertyName(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
