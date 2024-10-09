package com.calypso.binar.service.exception;

public class FileTooLargeException extends ServiceException{
    private static final String EXCEPTION_ID = "E024";
    public FileTooLargeException() {
        super("File too large", EXCEPTION_ID);
    }
}
