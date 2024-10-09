package com.calypso.binar.service.exception;

public class InvalidEmailException extends ServiceException{
    private static final String EXCEPTION_ID = "E003";
    public InvalidEmailException(String email) {
        super(email + " is not a valid email address!", EXCEPTION_ID);
    }
}
