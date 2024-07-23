package com.lamdangfixbug.qmshoe.exceptions;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder().statusCode(500).message("Some thing went wrong"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFondException(Exception ex, WebRequest request) {
        return  new ResponseEntity<>(ErrorDetails.builder().message(ex.getMessage()).statusCode(404).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException(Exception ex, WebRequest request) {
        if(ex instanceof UsernameNotFoundException) {
            return new ResponseEntity<>(ErrorDetails.builder().message("This email is not associated with any account!").statusCode(400).build(), HttpStatus.BAD_REQUEST);
        }
        if(ex instanceof BadCredentialsException) {
            return new ResponseEntity<>(ErrorDetails.builder().message("Email or password is invalid").statusCode(400).build(), HttpStatus.BAD_REQUEST);
        }
        if(ex instanceof AccessDeniedException) {
            return new ResponseEntity<>(ErrorDetails.builder().message("Access denied").statusCode(403).build(), HttpStatus.FORBIDDEN);
        }
        if(ex instanceof InsufficientAuthenticationException){
            return new ResponseEntity<>(ErrorDetails.builder().message("You don't have permission to perform this action").statusCode(403).build(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(ErrorDetails.builder().message("Bad request").statusCode(400).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtException(JwtException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder().message("Invalid token").statusCode(400).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistException(EmailAlreadyExistException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder().message("Email already exist").statusCode(400).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InsufficientInventoryException.class})
    public ResponseEntity<Object> handleInsufficientInventoryException(InsufficientAuthenticationException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder().message(ex.getMessage()).statusCode(400).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordDidNotMatchException.class)
    public ResponseEntity<Object> handlePasswordDidNotMatchException(PasswordDidNotMatchException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder().message(ex.getMessage()).statusCode(400).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UpdateOrderException.class)
    public ResponseEntity<Object> handleUpdateOrderException(UpdateOrderException ex, WebRequest request) {
        return new ResponseEntity<>(ErrorDetails.builder().message(ex.getMessage()).statusCode(400).build(), HttpStatus.BAD_REQUEST);
    }
}
