package com.georgen.hawthorne.model.sample;

import com.georgen.hawthorne.api.annotations.entities.MonoEntity;

@MonoEntity(name = "Sample")
public class Sample {
    private String field;

    public Sample(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
