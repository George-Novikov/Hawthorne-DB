package com.georgen.hawthorne.api.model;

import com.georgen.hawthorne.api.annotations.BinaryData;
import com.georgen.hawthorne.api.annotations.SingletonEntity;
import org.junit.jupiter.api.Disabled;

import java.util.Arrays;

@Disabled
@SingletonEntity
public class SingletonFileSample {
    private String field;
    @BinaryData
    private byte[] bytes;

    public String getField() { return field; }

    public void setField(String field) { this.field = field; }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public int hashCode() {
        int hashCode = 5;

        if (this.field != null) hashCode = 31 * hashCode + this.field.hashCode();
        if (this.bytes != null) hashCode = 31 * hashCode + this.bytes.hashCode();

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        boolean isInstance = obj instanceof SingletonFileSample;
        if (!isInstance) return false;

        SingletonFileSample other = (SingletonFileSample) obj;

        boolean isFieldEqual = (this.field == null && other.getField() == null)
                || (this.field != null && this.field.equals(other.getField()));

        boolean isBinaryDataEqual = (this.bytes == null && other.getBytes() == null)
                || (this.bytes != null && Arrays.equals(this.bytes, other.getBytes()));

        return isFieldEqual && isBinaryDataEqual;
    }
}
