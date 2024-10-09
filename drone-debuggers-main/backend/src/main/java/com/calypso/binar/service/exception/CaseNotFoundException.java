package com.calypso.binar.service.exception;

public class CaseNotFoundException extends ServiceException{
    private static final String EXCEPTION_ID = "E009";
    public CaseNotFoundException(String message) {
        super(message, EXCEPTION_ID);
    }
}
