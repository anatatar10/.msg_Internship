package com.calypso.binar.service.exception;

public class UserNotFoundException extends ServiceException {
    private static final String EXCEPTION_ID = "E001";
    public UserNotFoundException() {
        super("User does not exist", EXCEPTION_ID);
    }

}
