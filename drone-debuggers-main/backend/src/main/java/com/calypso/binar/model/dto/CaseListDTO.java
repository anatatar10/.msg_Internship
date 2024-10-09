package com.calypso.binar.model.dto;

public class CaseListDTO {
    private Integer caseListId;
    private String systemCaseId;
    private String reservationNumber;
    private String statusName;

    public CaseListDTO(Integer caseListId, String systemCaseId, String reservationNumber, String statusName) {
        this.caseListId = caseListId;
        this.systemCaseId = systemCaseId;
        this.reservationNumber = reservationNumber;
        this.statusName = statusName;
    }

    public Integer getCaseListId() {
        return caseListId;
    }

    public void setCaseListId(Integer caseListId) {
        this.caseListId = caseListId;
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

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
