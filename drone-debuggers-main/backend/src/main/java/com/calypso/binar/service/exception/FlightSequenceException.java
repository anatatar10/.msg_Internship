package com.calypso.binar.service.exception;

public class FlightSequenceException extends ServiceException{
    private static final String EXCEPTION_ID = "E027";
    public FlightSequenceException() {
        super("Flight Sequence Wrong", EXCEPTION_ID);
    }
}
