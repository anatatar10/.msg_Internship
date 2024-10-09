package com.calypso.binar.model;

import jakarta.persistence.*;


@Entity
@Table(name = "disruption")
public class Disruption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disruption_id")
    private int disruptionId;

    @ManyToOne
    @JoinColumn(name = "cancellation_type", nullable = false)
    private CancellationType cancellationType;

    @ManyToOne
    @JoinColumn(name = "disruption_option")
    private DisruptionOption disruptionOption;

    @ManyToOne
    @JoinColumn(name = "airline_motive")
    private AirlineMotive airlineMotive;

    @Column(name = "incident_description")
    private String incidentDescription;

    // Default constructor
    public Disruption() {}

    // Constructor with parameters
    public Disruption(CancellationType cancellationType, DisruptionOption disruptionOption, AirlineMotive airlineMotive, String incidentDescription) {
        this.cancellationType = cancellationType;
        this.disruptionOption = disruptionOption;
        this.airlineMotive = airlineMotive;
        this.incidentDescription = incidentDescription;
    }

    // Getters and Setters
    public int getDisruptionId() {
        return disruptionId;
    }

    public void setDisruptionId(int disruptionId) {
        this.disruptionId = disruptionId;
    }

    public CancellationType getCancellationType() {
        return cancellationType;
    }

    public void setCancellationType(CancellationType cancellationType) {
        this.cancellationType = cancellationType;
    }

    public DisruptionOption getDisruptionOption() {
        return disruptionOption;
    }

    public void setDisruptionOption(DisruptionOption disruptionOption) {
        this.disruptionOption = disruptionOption;
    }

    public AirlineMotive getAirlineMotive() {
        return airlineMotive;
    }

    public void setAirlineMotive(AirlineMotive airlineMotive) {
        this.airlineMotive = airlineMotive;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    // toString method
    @Override
    public String toString() {
        return "Disruption{" +
                "disruptionId=" + disruptionId +
                ", cancellationType=" + cancellationType +
                ", disruptionOption=" + disruptionOption +
                ", airlineMotive=" + airlineMotive +
                ", incidentDescription='" + incidentDescription + '\'' +
                '}';
    }
}
