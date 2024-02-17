package com.georgen.hawthorne.model.sample;

import java.util.Date;

public class SubSample {
    private String field;
    private Date date;

    public SubSample(){
        this.field = "SubSample field";
        this.date = new Date();
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
