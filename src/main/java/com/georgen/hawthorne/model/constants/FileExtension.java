package com.georgen.hawthorne.model.constants;

public enum FileExtension {
    ENTITY_EXTENSION("json"),
    BINARY_DATA_EXTENSION("bin");

    private String value;

    FileExtension(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
