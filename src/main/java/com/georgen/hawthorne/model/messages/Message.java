package com.georgen.hawthorne.model.messages;

public enum Message implements Descriptive {
    NOT_COMPATIBLE("File cannot be saved because it is not annotated as a Hawthorne entity."),
    NO_BINARY_DATA_ANNOTATION("The object data cannot be processed because the @BinaryData annotation is missing."),
    NOT_A_BYTE_ARRAY("The @BinaryData annotation is applicable only to fields of type byte[]."),
    BINARY_DATA_IS_NULL("The field of method annotated with @BinaryData is null."),
    BINARY_DATA_EXTRACTION_ERROR("Binary data extraction error."),
    NO_ENTITY_ANNOTATION("The object data cannot be processed because the @SingletonEntity or @EntityCollection annotations are missing."),
    ENTITY_RETRIEVAL_ERROR("Entity retrieval error."),
    NO_ID_ANNOTATION("The object cannot be processed because no fields or methods are marked with the @Id annotation."),
    FILE_IS_CORRUPTED("The file cannot be read because its structure is corrupted."),
    DELETE_FAIL("This file cannot be deleted because it does not exist."),
    SOURCE_IS_NULL("The operation cannot proceed because the StorageUnit source object is null.");

    private String description;

    Message(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
