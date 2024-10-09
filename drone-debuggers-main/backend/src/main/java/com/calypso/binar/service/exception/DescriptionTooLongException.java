package com.calypso.binar.service.exception;

public class DescriptionTooLongException extends ServiceException{
    private static final String EXCEPTION_ID = "E028";
    public DescriptionTooLongException() {
        super("Incident description too long.", EXCEPTION_ID);
    }

}
