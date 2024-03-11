package com.georgen.hawthorne.model.messages;

public enum Message implements Descriptive {
    NOT_ANNOTATED("File cannot be saved because it is not marked with the @SingletonEntity or @EntityCollection annotation."),
    NO_BINARY_DATA_ANNOTATION("The object data cannot be processed because the @BinaryData annotation is missing."),
    NOT_A_BYTE_ARRAY("The @BinaryData annotation is applicable only to fields or methods of type byte[]."),
    BINARY_DATA_IS_NULL("The field of method annotated with @BinaryData is null."),
    BINARY_DATA_EXTRACTION_ERROR("Binary data extraction error."),
    STORAGE_UNIT_INCONSISTENCY("The operation cannot proceed because StorageUnit is cast to the wrong type."),
    ENTITY_RETRIEVAL_ERROR("Entity retrieval error."),
    NO_ID_ANNOTATION("The object cannot be processed because no fields or methods are marked with the @Id annotation."),
    FILE_IS_CORRUPTED("The file cannot be read because its structure is corrupted."),
    DELETE_FAIL("This file cannot be deleted because it does not exist."),
    SOURCE_IS_NULL("The operation cannot proceed because the StorageUnit source object is null."),
    ID_COUNTER_ERROR("The ID counter is corrupted and cannot be read."),
    ID_IS_NULL("The request is missing the id parameter."),
    PERMISSION_DENIED("The operation is not permitted."),
    NOT_A_DIRECTORY("The requested path is not a directory."),
    NO_ENTITY_FILE("The entity file for this identifier was not found."),
    NO_BINARY_DATA_FILE("The requested object could not be retrieved because the binary data file is missing."),
    BINARY_DATA_IS_CORRUPTED("The requested object could not be retrieved because it's binary data file is corrupted."),
    NO_METHOD_PARAMETERS("A method annotated as @BinaryData must have at least one parameter."),
    TOO_MANY_METHOD_PARAMETERS("A method annotated as @BinaryData must have only one parameter."),
    ARCHETYPE_IS_NOT_REGISTERED("The requested entity has not yet been registered in the Hawthorne repository.")
    ;

    private String description;

    Message(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
