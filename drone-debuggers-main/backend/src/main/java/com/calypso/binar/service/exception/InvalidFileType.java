package com.calypso.binar.service.exception;

public class InvalidFileType extends ServiceException{
    private static final String EXCEPTION_ID = "E021";
    public InvalidFileType(String fileType) {
        super(fileType + " is not a valid file type!", EXCEPTION_ID);
    }
}
