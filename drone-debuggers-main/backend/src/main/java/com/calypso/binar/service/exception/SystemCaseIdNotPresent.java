package com.calypso.binar.service.exception;

public class SystemCaseIdNotPresent extends ServiceException {
    private static final String EXCEPTION_ID = "E026";
    public SystemCaseIdNotPresent(String systemCaseID) {
        super(systemCaseID + " does not exists", EXCEPTION_ID);
    }
}
