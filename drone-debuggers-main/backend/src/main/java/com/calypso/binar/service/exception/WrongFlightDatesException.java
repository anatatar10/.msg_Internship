package com.calypso.binar.service.exception;

public class WrongFlightDatesException extends ServiceException{
    private static final String EXCEPTION_ID = "E026";
    public WrongFlightDatesException() {
        super("Destination Date cannot be earlier than departing Date", EXCEPTION_ID);
    }
}
