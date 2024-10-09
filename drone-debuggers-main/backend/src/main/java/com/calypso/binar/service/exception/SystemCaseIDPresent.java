package com.calypso.binar.service.exception;

public class SystemCaseIDPresent extends ServiceException {
    private static final String EXCEPTION_ID = "E026";
    public SystemCaseIDPresent(String systemCaseID) {
        super(systemCaseID + " already exists", EXCEPTION_ID);
    }
}
