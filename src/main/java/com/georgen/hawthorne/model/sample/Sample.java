package com.georgen.hawthorne.model.sample;

import com.georgen.hawthorne.api.annotations.BinaryData;
import com.georgen.hawthorne.api.annotations.EntityCollection;
import com.georgen.hawthorne.api.annotations.Id;
import com.georgen.hawthorne.api.annotations.SingletonEntity;

@EntityCollection(path = "samples/example")
public class Sample {
    @Id
    private int id;
    private String field;
    @BinaryData
    private byte[] bytes;
    private SubSample subSample;

    public Sample() {}

    public Sample(String field) {
        this.field = field;
        this.bytes = field.getBytes();
        this.subSample = new SubSample();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public SubSample getSubSample() {
        return subSample;
    }

    public void setSubSample(SubSample subSample) {
        this.subSample = subSample;
    }
}
