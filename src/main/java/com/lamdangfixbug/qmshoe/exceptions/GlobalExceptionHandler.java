package com.lamdangfixbug.qmshoe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder().statusCode(500).message("Some thing went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ResourceNotFondException.class})
    public ResponseEntity<Object> handleResourceNotFondException(Exception ex, WebRequest request) {
        return  new ResponseEntity<>(ErrorDetails.builder().message(ex.getMessage()).statusCode(404).build(), HttpStatus.NOT_FOUND);
    }
}
