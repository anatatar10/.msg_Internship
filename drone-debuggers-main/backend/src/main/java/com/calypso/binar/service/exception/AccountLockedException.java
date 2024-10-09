package com.calypso.binar.service.exception;

public class AccountLockedException extends ServiceException{
    private static final String EXCEPTION_ID = "E008";
    public AccountLockedException() {
        super("Account is locked.", EXCEPTION_ID);
    }
}
