package com.calypso.binar.service.exception;


public class CompensationServiceException extends ServiceException{
    private static final String EXCEPTION_ID = "E006";
    public CompensationServiceException() {
        super("Failed to parse distance response", EXCEPTION_ID);
    }
}
