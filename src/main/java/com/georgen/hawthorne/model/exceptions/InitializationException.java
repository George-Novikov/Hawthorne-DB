package com.georgen.hawthorne.model.exceptions;

import com.georgen.hawthorne.model.messages.Descriptive;
import com.georgen.hawthorne.model.messages.ExceptionCaption;

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
                        ExceptionCaption.INITIALIZATION_EXCEPTION.getDescription(),
                        descriptive.getDescription()
                ),
                cause
        );
    }
}
