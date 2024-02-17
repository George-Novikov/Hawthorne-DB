package com.georgen.hawthorne.model.sample;

import com.georgen.hawthorne.api.annotations.BinaryData;
import com.georgen.hawthorne.api.annotations.entities.MonoEntity;

@MonoEntity(name = "Sample")
public class Sample {
    private String field;
    @BinaryData
    private byte[] bytes;
    private SubSample subSample;

    public Sample(String field) {
        this.field = field;
        this.bytes = field.getBytes();
        this.subSample = new SubSample();
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
