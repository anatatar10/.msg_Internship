package com.calypso.binar.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FlightInfoDTO {
    private int flightInfoId;
    @NotBlank
    private String airline;
    @NotNull
    private AirportDTO arrivalAirport;
    @NotNull
    private AirportDTO departureAirport;
    @PastOrPresent
    private Date arrivalDate;
    @PastOrPresent
    private Date departureDate;
    @NotBlank
    private String flightNumber;

    @JsonProperty("isProblematic")
    private boolean isProblematic;

    public FlightInfoDTO() {
    }

    public FlightInfoDTO(int flightInfoId, String airline, AirportDTO arrivalAirport, AirportDTO departureAirport, Date arrivalDate, Date departureDate, String flightNumber, boolean isProblematic) {
        this.flightInfoId = flightInfoId;
        this.airline = airline;
        this.arrivalAirport = arrivalAirport;
        this.departureAirport = departureAirport;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.flightNumber = flightNumber;
        this.isProblematic = isProblematic;
    }

    @Override
    public String toString() {
        return "FlightInfoDTO{" +
                "flightInfoId=" + flightInfoId +
                ", airline='" + airline + '\'' +
                ", arrivalAirport=" + arrivalAirport +
                ", departureAirport=" + departureAirport +
                ", arrivalDate=" + arrivalDate +
                ", departureDate=" + departureDate +
                ", flightNumber='" + flightNumber + '\'' +
                ", isProblematic=" + isProblematic +
                '}';
    }

    public int getFlightInfoId() {
        return flightInfoId;
    }

    public void setFlightInfoId(int flightInfoId) {
        this.flightInfoId = flightInfoId;
    }

    public @NotNull AirportDTO getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(@NotNull AirportDTO arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public @NotBlank String getAirline() {
        return airline;
    }

    public void setAirline(@NotBlank String airline) {
        this.airline = airline;
    }

    public @NotNull AirportDTO getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(@NotNull AirportDTO departureAirport) {
        this.departureAirport = departureAirport;
    }

    public @PastOrPresent Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(@PastOrPresent Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public @NotBlank String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(@NotBlank String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public @PastOrPresent Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(@PastOrPresent Date departureDate) {
        this.departureDate = departureDate;
    }

    public boolean isProblematic() {
        return isProblematic;
    }

    public void setProblematic(boolean problematic) {
        isProblematic = problematic;
    }
}
