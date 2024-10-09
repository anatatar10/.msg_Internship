package com.calypso.binar.service.exception;

public class DuplicateAirportException extends ServiceException{
    private static final String EXCEPTION_ID = "E023";
    public DuplicateAirportException() {
        super("Duplicate Airports", EXCEPTION_ID);
    }

}
