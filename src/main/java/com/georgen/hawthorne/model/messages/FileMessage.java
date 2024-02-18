package com.georgen.hawthorne.model.messages;

public enum FileMessage implements Descriptive {
    NOT_COMPATIBLE("File cannot be saved because it is not annotated as a Hawthorne entity."),
    NO_FILE_ANNOTATION("The object data cannot be processed because the @SingletonFile or @FileCollection annotations are missing."),
    NOT_A_BYTE_ARRAY("The @BinaryData annotation is applicable only to fields of type byte[]."),
    BINARY_DATA_IS_NULL("The field of method annotated with @BinaryData is null."),
    BINARY_DATA_EXTRACTION_ERROR("Binary data extraction error.");

    private String description;

    FileMessage(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
