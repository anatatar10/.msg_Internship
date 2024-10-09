package com.calypso.binar.service.exception;

public class DocumentsNotFoundException extends ServiceException{
    private static final String EXCEPTION_ID = "E014";
    public DocumentsNotFoundException() {
        super("Documents not found", EXCEPTION_ID);
    }
}
