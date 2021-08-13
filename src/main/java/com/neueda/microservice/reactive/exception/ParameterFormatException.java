package com.neueda.microservice.reactive.exception;

import static java.lang.String.format;

public class ParameterFormatException extends NumberFormatException {

    public ParameterFormatException(String parameterValue, String parserType, Throwable cause) {
        super(format("Unable to convert parameter string value '%s' into type '%s'", parameterValue, parserType));
        super.initCause(cause);
    }
}
