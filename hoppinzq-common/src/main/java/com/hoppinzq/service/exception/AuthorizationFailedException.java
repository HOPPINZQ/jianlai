package com.hoppinzq.service.exception;

/**
 * 授权异常
 */
public class AuthorizationFailedException extends Exception {
    public AuthorizationFailedException(String message) {
        super(message);
    }
}
