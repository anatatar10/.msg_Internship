package com.calypso.binar.service.exception;

public class ReservationNotFoundException extends ServiceException{
    private static final String EXCEPTION_ID = "E012";
    public ReservationNotFoundException() {
        super("Reservation not found", EXCEPTION_ID);
    }
}
