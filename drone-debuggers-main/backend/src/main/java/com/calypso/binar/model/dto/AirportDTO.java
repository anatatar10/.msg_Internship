package com.calypso.binar.model.dto;

public class AirportDTO {
    private String airportCode;
    private int airportId;
    private String airportName;
    private boolean deleted;

    public AirportDTO() {
    }

    public AirportDTO(int airportId,String airportCode, String airportName, boolean deleted) {
        this.airportCode = airportCode;
        this.airportId = airportId;
        this.airportName = airportName;
        this.deleted = deleted;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public int getAirportId() {
        return airportId;
    }

    public void setAirportId(int airportId) {
        this.airportId = airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
