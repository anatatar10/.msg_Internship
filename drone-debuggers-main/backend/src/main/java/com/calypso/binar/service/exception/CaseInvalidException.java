package com.calypso.binar.service.exception;

public class CaseInvalidException extends ServiceException{
    private static final String EXCEPTION_ID = "E007";
    public CaseInvalidException(String message) {
        super("Case is invalid", EXCEPTION_ID);
    }
}
