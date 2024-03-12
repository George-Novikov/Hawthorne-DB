package com.georgen.hawthorne.api.model;

import com.georgen.hawthorne.api.annotations.BinaryData;
import com.georgen.hawthorne.api.annotations.EntityCollection;
import com.georgen.hawthorne.api.annotations.Id;
import org.junit.jupiter.api.Disabled;

import java.util.Arrays;

@Disabled
@EntityCollection
public class FileCollectionSample {
    @Id
    private String uuid;
    private String field;
    @BinaryData
    private byte[] bytes;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public byte[] getBytes() { return bytes; }

    public void setBytes(byte[] bytes) { this.bytes = bytes; }

    @Override
    public int hashCode() {
        int hashCode = 5;

        if (this.uuid != null) hashCode = 31 * hashCode + this.uuid.hashCode();
        if (this.field != null) hashCode = 31 * hashCode + this.field.hashCode();
        if (this.bytes != null) hashCode = 31 * hashCode + this.bytes.hashCode();

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        boolean isInstance = obj instanceof FileCollectionSample;
        if (!isInstance) return false;

        FileCollectionSample other = (FileCollectionSample) obj;

        boolean isIdEqual = (this.uuid == null && other.getUuid() == null)
                || (this.uuid != null && this.uuid.equals(other.getUuid()));

        boolean isFieldEqual = (this.field == null && other.getField() == null)
                || (this.field != null && this.field.equals(other.getField()));

        boolean isBinaryDataEqual = (this.bytes == null && other.getBytes() == null)
                || (this.bytes != null && Arrays.equals(this.bytes, other.getBytes()));

        return isIdEqual && isFieldEqual && isBinaryDataEqual;
    }
}
