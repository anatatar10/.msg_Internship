package com.calypso.binar.service.exception;

public class ServiceException extends Exception{

    private final String exceptionID;
    public ServiceException(String message, String exceptionID) {
        super(message);
        this.exceptionID = exceptionID;
    }

    public String getExceptionID() {
        return exceptionID;
    }
}
