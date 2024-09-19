package com.lamdangfixbug.qmshoe.exceptions;

public class PasswordDidNotMatchException extends RuntimeException {
    public PasswordDidNotMatchException(String message) {
        super(message);
    }
}
