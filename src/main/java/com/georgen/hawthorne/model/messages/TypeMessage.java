package com.georgen.hawthorne.model.messages;

public enum TypeMessage implements Descriptive {
    WRONG_ID_TYPE("A field marked with the @Id annotation can only have String, int/Integer and long/Long data types.");
    private String description;

    TypeMessage(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
