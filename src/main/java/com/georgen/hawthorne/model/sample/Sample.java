package com.georgen.hawthorne.model.sample;

import com.georgen.hawthorne.api.annotations.BinaryData;
import com.georgen.hawthorne.api.annotations.EntityCollection;
import com.georgen.hawthorne.api.annotations.Id;
import com.georgen.hawthorne.api.annotations.SingletonEntity;
import jakarta.persistence.Entity;

@SingletonEntity(path = "samples/example")
public class Sample {
    @BinaryData
    private byte[] bytes;

    public byte[] getBytes() { return bytes; }

    public void setBytes(byte[] bytes) { this.bytes = bytes; }
}
