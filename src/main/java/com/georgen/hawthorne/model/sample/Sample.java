package com.georgen.hawthorne.model.sample;

import com.georgen.hawthorne.api.annotations.BinaryData;
import com.georgen.hawthorne.api.annotations.EntityCollection;
import com.georgen.hawthorne.api.annotations.Id;
import com.georgen.hawthorne.api.annotations.SingletonEntity;
import jakarta.persistence.Entity;

@EntityCollection(path = "samples/example")
public class Sample {
    @Id
    private String uuid;
    @BinaryData
    private byte[] bytes;

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public byte[] getBytes() { return bytes; }

    public void setBytes(byte[] bytes) { this.bytes = bytes; }
}
