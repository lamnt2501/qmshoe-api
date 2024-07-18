package com.lamdangfixbug.qmshoe.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InsufficientInventoryException extends RuntimeException {
    public InsufficientInventoryException(String msg) {
        super(msg);
    }
}
