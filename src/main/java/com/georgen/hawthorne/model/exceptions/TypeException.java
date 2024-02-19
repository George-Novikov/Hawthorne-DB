package com.georgen.hawthorne.model.exceptions;

import com.georgen.hawthorne.model.messages.Descriptive;

public class TypeException extends Exception {
    public TypeException(Descriptive descriptive){
        super(descriptive.getDescription());
    }

    public TypeException(Throwable cause){
        super(cause);
    }

    public TypeException(String message, Throwable cause){
        super(message, cause);
    }
}
