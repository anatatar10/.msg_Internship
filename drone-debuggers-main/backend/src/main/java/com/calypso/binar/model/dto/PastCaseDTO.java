package com.calypso.binar.model.dto;

public class PastCaseDTO {
    private int caseId;
    private String systemCaseId;
    private String reservationNumber;
    private String passengerFirstName;
    private String passengerLastName;
    private String status;
    private String assignedColleagueEmail;
    private String assignedColleagueFirstName;
    private String assignedColleagueLastName;

    public PastCaseDTO() {
    }

    public PastCaseDTO(int caseId, String systemCaseId, String reservationNumber, String passengerFirstName, String passengerLastName, String status, String assignedColleagueEmail, String assignedColleagueFirstName, String assignedColleagueLastName) {
        this.caseId = caseId;
        this.systemCaseId = systemCaseId;
        this.reservationNumber = reservationNumber;
        this.passengerFirstName = passengerFirstName;
        this.passengerLastName = passengerLastName;
        this.status = status;
        this.assignedColleagueEmail = assignedColleagueEmail;
        this.assignedColleagueFirstName = assignedColleagueFirstName;
        this.assignedColleagueLastName = assignedColleagueLastName;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getSystemCaseId() {
        return systemCaseId;
    }

    public void setSystemCaseId(String systemCaseId) {
        this.systemCaseId = systemCaseId;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getPassengerFirstName() {
        return passengerFirstName;
    }

    public void setPassengerFirstName(String passengerFirstName) {
        this.passengerFirstName = passengerFirstName;
    }

    public String getPassengerLastName() {
        return passengerLastName;
    }

    public void setPassengerLastName(String passengerLastName) {
        this.passengerLastName = passengerLastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedColleagueFirstName() {
        return assignedColleagueFirstName;
    }

    public void setAssignedColleagueFirstName(String assignedColleagueFirstName) {
        this.assignedColleagueFirstName = assignedColleagueFirstName;
    }

    public String getAssignedColleagueEmail() {
        return assignedColleagueEmail;
    }

    public void setAssignedColleagueEmail(String assignedColleagueEmail) {
        this.assignedColleagueEmail = assignedColleagueEmail;
    }

    public String getAssignedColleagueLastName() {
        return assignedColleagueLastName;
    }

    public void setAssignedColleagueLastName(String assignedColleagueLastName) {
        this.assignedColleagueLastName = assignedColleagueLastName;
    }

    @Override
    public String toString() {
        return "PastCaseDTO{" +
                "caseId=" + caseId +
                ", systemCaseId='" + systemCaseId + '\'' +
                ", reservationNumber='" + reservationNumber + '\'' +
                ", passengerFirstName='" + passengerFirstName + '\'' +
                ", passengerLastName='" + passengerLastName + '\'' +
                ", status='" + status + '\'' +
                ", assignedColleagueEmail='" + assignedColleagueEmail + '\'' +
                ", assignedColleagueFirstName='" + assignedColleagueFirstName + '\'' +
                ", assignedColleagueLastName='" + assignedColleagueLastName + '\'' +
                '}';
    }
}
