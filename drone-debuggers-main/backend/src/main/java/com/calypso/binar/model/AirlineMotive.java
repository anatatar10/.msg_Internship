package com.calypso.binar.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "airline_motive")
public class AirlineMotive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airline_motive_id")
    private int airlineMotiveId;

    @Column(name = "airline_motive_type_description", nullable = false)
    private String airlineMotiveTypeDescription;

    // Default constructor
    public AirlineMotive() {}

    // Constructor with parameters
    public AirlineMotive(String airlineMotiveTypeDescription) {
        this.airlineMotiveTypeDescription = airlineMotiveTypeDescription;
    }

    // Getters and Setters
    public int getAirlineMotiveId() {
        return airlineMotiveId;
    }

    public void setAirlineMotiveId(int airlineMotiveId) {
        this.airlineMotiveId = airlineMotiveId;
    }

    public String getAirlineMotiveTypeDescription() {
        return airlineMotiveTypeDescription;
    }

    public void setAirlineMotiveTypeDescription(String airlineMotiveTypeDescription) {
        this.airlineMotiveTypeDescription = airlineMotiveTypeDescription;
    }

    // toString method
    @Override
    public String toString() {
        return "AirlineMotive{" +
                "airlineMotiveId=" + airlineMotiveId +
                ", airlineMotiveTypeDescription='" + airlineMotiveTypeDescription + '\'' +
                '}';
    }
}
