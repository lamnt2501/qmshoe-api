package com.lamdangfixbug.qmshoe.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InsufficientInventoryException extends AuthenticationException {
    public InsufficientInventoryException(String msg) {
        super(msg);
    }
}
