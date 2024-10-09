package com.calypso.binar.service.exception;

public class PassengerDetailsNotFoundException extends ServiceException{
    private static final String EXCEPTION_ID = "E013";
    public PassengerDetailsNotFoundException() {
        super("Passenger details not found", EXCEPTION_ID);
    }
}
