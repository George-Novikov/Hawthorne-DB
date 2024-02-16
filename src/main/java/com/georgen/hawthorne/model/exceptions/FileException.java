package com.georgen.hawthorne.model.exceptions;

import com.georgen.hawthorne.model.messages.Descriptive;

import java.io.File;

public class FileException extends Exception {
    public FileException(Descriptive descriptive){
        super(descriptive.getDescription());
    }

    public FileException(Throwable cause){
        super(cause);
    }

    public FileException(String message, Throwable cause){
        super(message, cause);
    }
}
