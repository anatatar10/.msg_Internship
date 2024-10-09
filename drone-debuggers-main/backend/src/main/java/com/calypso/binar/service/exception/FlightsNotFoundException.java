package com.calypso.binar.service.exception;

public class FlightsNotFoundException extends ServiceException{
    private static final String EXCEPTION_ID = "E022";
    public FlightsNotFoundException() {
        super("Flights not found", EXCEPTION_ID);
    }
}
