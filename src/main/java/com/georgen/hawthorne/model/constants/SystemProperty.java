package com.georgen.hawthorne.model.constants;

public enum SystemProperty {
    APPLICATION_PROPERTIES_NAME("application.properties"),
    APPLICATION_YAML_NAME("src/main/resources/application.yaml"),
    APPLICATION_YML_NAME("src/main/resources/application.yml"),

    HAWTHORNE_PROPERTIES_NAME("hawthorne.properties"),
    HAWTHORNE_YAML_NAME("hawthorne.yaml"),
    HAWTHORNE_YML_NAME("hawthorne.yml"),

    ID_COUNTER_NAME("id-counter"),
    UUID_INDEX_NAME("uuid-index"),

    ENTITY_FILE_EXTENSION("json"),
    BINARY_DATA_EXTENSION("bin");

    private String value;

    SystemProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
