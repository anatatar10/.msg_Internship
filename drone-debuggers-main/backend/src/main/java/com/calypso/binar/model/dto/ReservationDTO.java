package com.calypso.binar.model.dto;

import com.calypso.binar.model.Airport;

public class ReservationDTO {

    private int reservationId;
    private String reservationNumber;
    private Airport departingAirport;
    private Airport destinationAirport;

    public ReservationDTO() {
    }

    public ReservationDTO(int reservationId, String reservationNumber, Airport departingAirport, Airport destinationAirport) {
        this.reservationId = reservationId;
        this.reservationNumber = reservationNumber;
        this.departingAirport = departingAirport;
        this.destinationAirport = destinationAirport;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public Airport getDepartingAirport() {
        return departingAirport;
    }

    public void setDepartingAirport(Airport departingAirport) {
        this.departingAirport = departingAirport;
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airport destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "reservationId=" + reservationId +
                ", reservationNumber='" + reservationNumber + '\'' +
                ", departingAirport='" + departingAirport + '\'' +
                ", destinationAirport='" + destinationAirport + '\'' +
                '}';
    }
}
