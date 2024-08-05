package com.example.identity_service.exception;

import java.io.Serial;

import org.springframework.security.core.AuthenticationException;

public final class UnusualLocationException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 5861310537366287163L;

    public UnusualLocationException(final String message) {
        super(message);
    }
}
