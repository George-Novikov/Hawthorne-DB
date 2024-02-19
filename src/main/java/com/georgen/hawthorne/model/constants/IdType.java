package com.georgen.hawthorne.model.constants;

import com.georgen.hawthorne.model.exceptions.TypeException;

public enum IdType {
    UUID,
    INTEGER,
    LONG,
    NONE;

    public static IdType of(Class javaClass) throws TypeException {
        if (javaClass.equals(String.class)) return UUID;
        if (javaClass.equals(Integer.class) || javaClass.equals(int.class)) return INTEGER;
        if (javaClass.equals(Long.class) || javaClass.equals(long.class)) return LONG;
        return NONE;
    }
}
