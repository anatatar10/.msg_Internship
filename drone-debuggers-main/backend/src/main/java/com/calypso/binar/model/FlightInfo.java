package com.calypso.binar.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "flight_info")
public class FlightInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_info_id")
    private int flightInfoId;

    @ManyToOne()
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "airline", nullable = false)
    private String airline;

    @Column(name = "planned_departure_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date plannedDepartureDate;

    @Column(name = "planned_arrival_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date plannedArrivalDate;

    @Column(name = "flight_nr", nullable = false)
    private String flightNr;

    @ManyToOne()
    @JoinColumn(name = "departing_airport", nullable = false)
    private Airport departingAirport;

    @ManyToOne()
    @JoinColumn(name = "destination_airport", nullable = false)
    private Airport destinationAirport;

    @Column(name = "problem_flight", nullable = false)
    private boolean problemFlight;

    // Default constructor
    public FlightInfo() {
    }

    // Constructor with parameters
    public FlightInfo(Reservation reservation, String airline, Date plannedDepartureDate, Date plannedArrivalDate, String flightNr, Airport departingAirport, Airport destinationAirport, boolean problemFlight) {
        this.reservation = reservation;
        this.airline = airline;
        this.plannedDepartureDate = plannedDepartureDate;
        this.plannedArrivalDate = plannedArrivalDate;
        this.flightNr = flightNr;
        this.departingAirport = departingAirport;
        this.destinationAirport = destinationAirport;
        this.problemFlight = problemFlight;
    }

    public int getFlightInfoId() {
        return flightInfoId;
    }

    public void setFlightInfoId(int flightInfoId) {
        this.flightInfoId = flightInfoId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public Date getPlannedDepartureDate() {
        return plannedDepartureDate;
    }

    public void setPlannedDepartureDate(Date plannedDepartureDate) {
        this.plannedDepartureDate = plannedDepartureDate;
    }

    public Date getPlannedArrivalDate() {
        return plannedArrivalDate;
    }

    public void setPlannedArrivalDate(Date plannedArrivalDate) {
        this.plannedArrivalDate = plannedArrivalDate;
    }

    public String getFlightNr() {
        return flightNr;
    }

    public void setFlightNr(String flightNr) {
        this.flightNr = flightNr;
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

    public boolean isProblemFlight() {
        return problemFlight;
    }

    public void setProblemFlight(boolean problemFlight) {
        this.problemFlight = problemFlight;
    }

    @Override
    public String toString() {
        return "FlightInfo{" +
                "flightInfoId=" + flightInfoId +
                ", reservation=" + reservation +
                ", airline='" + airline + '\'' +
                ", plannedDepartureDate=" + plannedDepartureDate +
                ", plannedArrivalDate=" + plannedArrivalDate +
                ", flightNr='" + flightNr + '\'' +
                ", departingAirport=" + departingAirport +
                ", destinationAirport=" + destinationAirport +
                ", problemFlight=" + problemFlight +
                '}';
    }
}