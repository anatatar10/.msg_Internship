package com.calypso.binar.service.exception;

public class StatusNonExistent extends ServiceException {
    private static final String EXCEPTION_ID = "E025";
    public StatusNonExistent(String status) {
        super(status + " does not exist", EXCEPTION_ID);
    }
}
