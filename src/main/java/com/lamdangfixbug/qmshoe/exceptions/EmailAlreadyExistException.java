package com.lamdangfixbug.qmshoe.exceptions;

import org.springframework.security.core.AuthenticationException;

public class EmailAlreadyExistException extends AuthenticationException {
    public EmailAlreadyExistException() {
        super("Email already exist");
    }
}
