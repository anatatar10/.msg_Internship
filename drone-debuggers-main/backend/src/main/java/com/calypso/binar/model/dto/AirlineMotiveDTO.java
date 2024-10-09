package com.calypso.binar.model.dto;

import jakarta.validation.constraints.NotBlank;

public class AirlineMotiveDTO {

    @NotBlank
    private String airlineMotiveTypeDescription;

    public AirlineMotiveDTO() {
    }

    public AirlineMotiveDTO( String airlineMotiveTypeDescription) {
        this.airlineMotiveTypeDescription = airlineMotiveTypeDescription;
    }

    public String getAirlineMotiveTypeDescription() {
        return airlineMotiveTypeDescription;
    }

    public void setAirlineMotiveTypeDescription(String airlineMotiveTypeDescription) {
        this.airlineMotiveTypeDescription = airlineMotiveTypeDescription;
    }

    @Override
    public String toString() {
        return "AirlineMotiveDTO{" +
                ", airlineMotiveTypeDescription='" + airlineMotiveTypeDescription + '\'' +
                '}';
    }
}
