package com.georgen.hawthorne.model.messages;

public enum SystemMessage implements Descriptive {
    MANAGING_FILE_LOAD_FAIL("failed to load managing file");

    private String description;

    SystemMessage(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
