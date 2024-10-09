package com.calypso.binar.service.exception;

public class CommentsNotFoundException extends ServiceException{
    private static final String EXCEPTION_ID = "E011";
    public CommentsNotFoundException() {
        super("Comments not found", EXCEPTION_ID);
    }
}
