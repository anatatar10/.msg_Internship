package com.calypso.binar.service.exception;

public class FailedAuthenticationException extends ServiceException{
    private static final String EXCEPTION_ID = "E004";
    public FailedAuthenticationException() {
        super("Authentication failed, username or password wrong.", EXCEPTION_ID);
    }
}
