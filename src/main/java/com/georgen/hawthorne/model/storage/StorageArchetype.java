package com.georgen.hawthorne.model.storage;

import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.exceptions.FileException;
import com.georgen.hawthorne.tools.PathBuilder;

public class StorageArchetype {
    private String simpleName;
    private String fullName;
    private EntityType entityType;
    private String path;
    private int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int hashCode() {
        int hashCode = 5;

        if (this.simpleName != null) hashCode = 31 * hashCode + this.simpleName.hashCode();
        if (this.fullName != null) hashCode = 31 * hashCode + this.fullName.hashCode();
        if (this.entityType != null) hashCode = 31 * hashCode + this.entityType.hashCode();
        if (this.path != null) hashCode = 31 * hashCode + this.path.hashCode();

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        boolean isInstance = obj instanceof StorageArchetype;
        if (!isInstance) return false;

        StorageArchetype other = (StorageArchetype) obj;

        boolean isSimpleNameEqual = (this.simpleName == null && other.getSimpleName() == null)
                || (this.simpleName != null && this.simpleName.equals(other.getSimpleName()));

        boolean isFullNameEqual = (this.fullName == null && other.getFullName() == null)
                || (this.fullName != null && this.fullName.equals(other.getFullName()));

        boolean isEntityTypeEqual = (this.entityType == null && other.getEntityType() == null)
                || (this.entityType != null && this.entityType.equals(other.getEntityType()));

        boolean isPathEqual = (this.path == null && other.getPath() == null)
                || (this.path != null && this.path.equals(other.getPath()));

        return isSimpleNameEqual && isFullNameEqual && isEntityTypeEqual && isPathEqual;
    }
}
