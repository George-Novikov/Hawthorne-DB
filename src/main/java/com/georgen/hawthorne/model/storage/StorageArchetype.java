package com.georgen.hawthorne.model.storage;

import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.exceptions.FileException;
import com.georgen.hawthorne.tools.PathBuilder;

public class StorageArchetype {
    private String simpleName;
    private String fullName;
    private EntityType entityType;
    private String path;

    public StorageArchetype(Object object) throws FileException {
        Class javaClass = object.getClass();

        this.simpleName = javaClass.getSimpleName();
        this.fullName = javaClass.getName();
        this.entityType = EntityType.of(javaClass);
        this.path = PathBuilder.getPath(this.simpleName, this.entityType);
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
