package com.calypso.binar.service.exception;

public class RoleDoesNotExistException extends ServiceException{
    private static final String EXCEPTION_ID = "E001";
    public RoleDoesNotExistException(String role) {
        super(role + " does not exist", EXCEPTION_ID);
    }
}
