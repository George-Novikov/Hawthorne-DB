package com.georgen.hawthorne.model.exceptions;

import com.georgen.hawthorne.model.messages.Descriptive;
import com.georgen.hawthorne.model.messages.ExceptionCaption;

public class HawthorneException extends Exception {
    public HawthorneException(Descriptive descriptive){
        super(descriptive.getDescription());
    }

    public HawthorneException(Throwable cause){
        super(cause);
    }

    public HawthorneException(String message, Throwable cause){
        super(message, cause);
    }

    public HawthorneException(Descriptive descriptive, Throwable cause){
        this(
                String.format(
                        ExceptionCaption.HAWTHORNE_EXCEPTION.getDescription(),
                        descriptive.getDescription()
                ),
                cause
        );
    }
}
