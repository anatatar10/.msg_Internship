package com.calypso.binar.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compensation_case")
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_id")
    private int caseId;

    @Column(name = "system_case_id", nullable = false)
    private String systemCaseId;

    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;

    @ManyToOne()
    @JoinColumn(name = "status", nullable = false)
    private Status status;

    @ManyToOne()
    @JoinColumn(name = "colleague")
    private User colleague;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne()
    @JoinColumn(name = "passenger_details_id")
    private PassengerDetails passengerDetails;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "disruption_id")
    private Disruption disruption;

    @ManyToOne()
    @JoinColumn(name = "passenger", nullable = false)
    private User passenger;

    @Column(name = "compensation", nullable = false)
    private int compensation;


    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GeneratedPdf> generatedPdfs = new ArrayList<>();

    // Default constructor
    public Case() {}

    // Constructor with parameters
    public Case(String systemCaseId,Timestamp dateCreated, Status status, User colleague, Reservation reservation, PassengerDetails passengerDetails, Disruption disruption, User passenger, int compensation) {
        this.systemCaseId=systemCaseId;
        this.dateCreated = dateCreated;
        this.status = status;
        this.colleague = colleague;
        this.reservation = reservation;
        this.passengerDetails = passengerDetails;
        this.disruption = disruption;
        this.passenger = passenger;
        this.compensation = compensation;
    }

    // Getters and Setters
    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getColleague() {
        return colleague;
    }

    public void setColleague(User colleague) {
        this.colleague = colleague;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public PassengerDetails getPassengerDetails() {
        return passengerDetails;
    }

    public void setPassengerDetails(PassengerDetails passengerDetails) {
        this.passengerDetails = passengerDetails;
    }

    public Disruption getDisruption() {
        return disruption;
    }

    public void setDisruption(Disruption disruption) {
        this.disruption = disruption;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public int getCompensation() {
        return compensation;
    }

    public void setCompensation(int compensation) {
        this.compensation = compensation;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getSystemCaseId() {
        return systemCaseId;
    }

    public void setSystemCaseId(String systemCaseId) {
        this.systemCaseId = systemCaseId;
    }

    @Override
    public String toString() {
        return "Case{" +
                "compensation=" + compensation +
                ", systemCaseId='" + systemCaseId + '\'' +
                ", dateCreated=" + dateCreated +
                ", status=" + status +
                ", colleague=" + colleague +
                ", reservation=" + reservation +
                ", passengerDetails=" + passengerDetails +
                ", disruption=" + disruption +
                ", passenger=" + passenger +
                ", comments=" + comments +
                '}';
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null || getClass() != obj.getClass()){
            return false;
        }
        Case aCase = (Case) obj;
        return caseId == aCase.caseId;
    }
}
