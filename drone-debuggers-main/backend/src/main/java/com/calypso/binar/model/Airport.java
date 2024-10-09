package com.calypso.binar.model;

import jakarta.persistence.*;

@Entity
@Table(name = "airport")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airport_id")
    private Integer airportId;

    @Column(name = "airport_name", nullable = false)
    private String airportName;

    @Column(name = "airport_code", nullable = false, length = 3)
    private String airportCode;

    @Column(name = "deleted")
    private boolean deleted = false;

    // Default constructor
    public Airport() {
    }

    // Parameterized constructor
    public Airport(String airportName, String airportCode) {
        this.airportName = airportName;
        this.airportCode = airportCode;
    }

    // Getters and Setters
    public Integer getAirportId() {
        return airportId;
    }

    public void setAirportId(Integer airportId) {
        this.airportId = airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }



    @Override
    public String toString() {
        return "Airport{" +
                "airportId=" + airportId +
                ", airportName='" + airportName + '\'' +
                ", airportCode='" + airportCode + '\'' +
                '}';
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Airport other = (Airport) obj;
        if (airportId == null) {
            if (other.airportId != null)
                return false;
        } else if (!airportId.equals(other.airportId))
            return false;
        if (airportName == null) {
            if (other.airportName != null)
                return false;
        } else if (!airportName.equals(other.airportName))
            return false;
        if (airportCode == null) {
            if (other.airportCode != null)
                return false;
        } else if (!airportCode.equals(other.airportCode))
            return false;
        return true;
    }
}

