package com.calypso.binar.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private int reservationId;

    @Column(name = "reservation_number", nullable = false)
    private String reservationNumber;

    @ManyToOne()
    @JoinColumn(name = "departing_airport", nullable = false)
    private Airport departingAirport;

    @ManyToOne()
    @JoinColumn(name = "destination_airport", nullable = false)
    private Airport destinationAirport;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightInfo> flightInfo = new ArrayList<>();

    // Default constructor
    public Reservation() {}

    // Constructor with parameters
    public Reservation(String reservationNumber, Airport departingAirport, Airport destinationAirport) {
        this.reservationNumber = reservationNumber;
        this.departingAirport = departingAirport;
        this.destinationAirport = destinationAirport;
    }

    // Getters and Setters
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

    public List<FlightInfo> getFlightInfo() {
        return flightInfo;
    }

    public void setFlightInfo(List<FlightInfo> flightInfo) {
        this.flightInfo = flightInfo;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", reservationNumber='" + reservationNumber + '\'' +
                ", departingAirport=" + departingAirport +
                ", destinationAirport=" + destinationAirport +
                '}';
    }



}