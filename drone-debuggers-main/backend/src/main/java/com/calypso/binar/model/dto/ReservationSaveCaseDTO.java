package com.calypso.binar.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReservationSaveCaseDTO {

    @NotNull
    private AirportDTO departingAirport;
    @NotNull
    private AirportDTO arrivingAirport;
    @NotNull
    private int possibleCompensation;
    @NotBlank
    @Pattern(regexp = "^[0-9A-Z]{6}$")
    private String reservationNumber;
    private List<AirportDTO> stops;

    public ReservationSaveCaseDTO() {
    }

    public ReservationSaveCaseDTO(AirportDTO departingAirport, AirportDTO arrivingAirport, int possibleCompensation, String reservationNumber, List<AirportDTO> stops) {
        this.departingAirport = departingAirport;
        this.arrivingAirport = arrivingAirport;
        this.possibleCompensation = possibleCompensation;
        this.reservationNumber = reservationNumber;
        this.stops = stops;
    }

    @Override
    public String toString() {
        return "ReservationSaveCaseDTO{" +
                "departingAirport=" + departingAirport +
                ", arrivingAirport=" + arrivingAirport +
                ", possibleCompensation=" + possibleCompensation +
                ", reservationNumber='" + reservationNumber + '\'' +
                ", stops=" + stops +
                '}';
    }
}
