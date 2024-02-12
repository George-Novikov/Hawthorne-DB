package com.georgen.hawthorne.model.exceptions;

import com.georgen.hawthorne.model.messages.Descriptive;

public class InitializationException extends RuntimeException {

    public InitializationException(String message){
        super(message);
    }

    public InitializationException(Throwable cause){
        super(cause);
    }

    public InitializationException(String message, Throwable cause){
        super(message, cause);
    }

    public InitializationException(Descriptive descriptive, Throwable cause){
        super(
                String.format(
                        "Hawthorne initialization error: %s",
                        descriptive.getDescription()
                ),
                cause
        );
    }
}
