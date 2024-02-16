package com.georgen.hawthorne.model.messages;

public enum FileMessage implements Descriptive {
    FILE_IS_NOT_AN_ENTITY("File cannot be saved because it's not a Hawthorne entity.");

    private String description;

    FileMessage(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
