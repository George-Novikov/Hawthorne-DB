package com.georgen.hawthorne.api.model;

import com.georgen.hawthorne.api.annotations.SingletonEntity;
import org.junit.jupiter.api.Disabled;

@Disabled
@SingletonEntity
public class SingletonEntitySample {
    private String field;

    public String getField() { return field; }

    public void setField(String field) { this.field = field; }

    @Override
    public int hashCode() {
        int hashCode = 5;

        if (this.field != null) hashCode = 31 * hashCode + this.field.hashCode();

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        boolean isInstance = obj instanceof SingletonEntitySample;
        if (!isInstance) return false;

        SingletonEntitySample other = (SingletonEntitySample) obj;

        boolean isFieldEqual = (this.field == null && other.getField() == null)
                || (this.field != null && this.field.equals(other.getField()));

        return isFieldEqual;
    }
}
