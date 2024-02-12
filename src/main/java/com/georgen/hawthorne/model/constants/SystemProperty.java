package com.georgen.hawthorne.model.constants;

public enum SystemProperty {
    CONFIG_FILE_NAME("hawthorne.properties");

    private String value;

    SystemProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
