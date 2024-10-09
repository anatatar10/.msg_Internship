package com.calypso.binar.service.exception;

public class NonUniqueEmailException extends ServiceException{
    private static final String EXCEPTION_ID = "E002";
    public NonUniqueEmailException(String email) {
        super(email + " is already in use!", EXCEPTION_ID);
    }
}
