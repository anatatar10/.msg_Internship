package com.calypso.binar.service.exception;

public class DuplicateFlightNumberException extends ServiceException{
    private static final String EXCEPTION_ID = "E025";
    public DuplicateFlightNumberException() {
        super("Duplicate Flight Numbers", EXCEPTION_ID);
    }

}
