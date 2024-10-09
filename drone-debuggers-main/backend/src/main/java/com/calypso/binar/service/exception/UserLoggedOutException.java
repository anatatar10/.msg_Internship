package com.calypso.binar.service.exception;

public class UserLoggedOutException extends ServiceException{
    private static final String EXCEPTION_ID = "E015";
    public UserLoggedOutException() {
        super("User logged out.", EXCEPTION_ID);
    }
}
