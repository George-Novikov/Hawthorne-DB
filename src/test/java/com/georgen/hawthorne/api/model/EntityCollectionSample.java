package com.georgen.hawthorne.api.model;

import com.georgen.hawthorne.api.annotations.EntityCollection;
import com.georgen.hawthorne.api.annotations.Id;
import org.junit.jupiter.api.Disabled;

@Disabled
@EntityCollection
public class EntityCollectionSample {
    @Id
    private Integer id;
    private String field;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public int hashCode() {
        int hashCode = 5;

        if (this.id != null) hashCode = 31 * hashCode + this.id.hashCode();
        if (this.field != null) hashCode = 31 * hashCode + this.field.hashCode();

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        boolean isInstance = obj instanceof EntityCollectionSample;
        if (!isInstance) return false;

        EntityCollectionSample other = (EntityCollectionSample) obj;

        boolean isIdEqual = (this.id == null && other.getId() == null)
                || (this.id != null && this.id.equals(other.getId()));

        boolean isFieldEqual = (this.field == null && other.getField() == null)
                || (this.field != null && this.field.equals(other.getField()));

        return isIdEqual && isFieldEqual;
    }
}
