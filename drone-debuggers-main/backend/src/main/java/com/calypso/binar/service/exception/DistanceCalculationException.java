package com.calypso.binar.service.exception;

public class DistanceCalculationException extends ServiceException{
    private static final String EXCEPTION_ID = "E005";
    public DistanceCalculationException() {
        super("Failed to calculate distance", EXCEPTION_ID);
    }
}
