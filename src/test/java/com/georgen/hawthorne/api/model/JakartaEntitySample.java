package com.georgen.hawthorne.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Disabled;

import java.time.LocalDateTime;

@Disabled
@Entity
public class JakartaEntitySample {
    @Id
    private Integer id;
    private String field;
    private LocalDateTime date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String text) {
        this.field = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hashCode = 5;

        if (this.id != null) hashCode = 31 * hashCode + this.id.hashCode();
        if (this.field != null) hashCode = 31 * hashCode + this.field.hashCode();
        if (this.date != null) hashCode = 31 * hashCode + this.date.hashCode();

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        boolean isInstance = obj instanceof JakartaEntitySample;
        if (!isInstance) return false;

        JakartaEntitySample other = (JakartaEntitySample) obj;

        boolean isIdEqual = (this.id == null && other.getId() == null)
                || (this.id != null && this.id.equals(other.getId()));

        boolean isFieldEqual = (this.field == null && other.getField() == null)
                || (this.field != null && this.field.equals(other.getField()));

        boolean isDateEqual = (this.date == null && other.getDate() == null)
                || (this.date != null && this.date.equals(other.getDate()));

        return isIdEqual && isFieldEqual && isDateEqual;
    }
}
