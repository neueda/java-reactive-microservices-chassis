package com.neueda.microservice.reactive.chassis.exception;

import com.neueda.microservice.reactive.chassis.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(IdFormatException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    private ErrorResponse handleNumberFormatError(IdFormatException ex) {
        return logAndRespond(ex, ex.getPath());
    }

    @ExceptionHandler(MandatoryPathParameterException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse handleMandatoryPathParameter(MandatoryPathParameterException ex) {
        return logAndRespond(ex, ex.getPath());
    }

    private ErrorResponse logAndRespond(Exception ex, String path) {
        String errorMsg = ex.getLocalizedMessage();
        log.error(errorMsg, ex);

        return new ErrorResponse(errorMsg, path, ex.getClass().getTypeName());
    }
}
