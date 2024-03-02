package com.georgen.hawthorne.model.constants;

public enum ConfigProperty {
    CONTROL_FILE_NAME("hawthorne.naming.control-file", "storage-schema.json"),
    ROOT_PATH("hawthorne.naming.root-directory", "hawthorne"),
    ENTITIES_PATH("hawthorne.naming.entities-directory", "entities"),
    PARTITIONING_THRESHOLD("hawthorne.storage.partitioning-threshold", "50000")
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
