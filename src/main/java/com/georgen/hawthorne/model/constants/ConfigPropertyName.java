package com.georgen.hawthorne.model.constants;

public enum ConfigPropertyName {
    MANAGING_FILE_NAME("hawthorne.naming.control-file"),
    PATHS_FILE_NAME("hawthorne.naming.paths-file"),
    SETTINGS_FILE_NAME("hawthorne.naming.settings-file"),
    ROOT_DIRECTORY_NAME("hawthorne.naming.root-directory")
    ;

    private String name;

    ConfigPropertyName(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
