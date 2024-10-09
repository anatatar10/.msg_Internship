package com.calypso.binar.model.dto;

import java.util.Date;


public class PDFShowHistoryDTO {
    private int documentId;
    private String documentName;
    private String passengerFirstName;
    private String passengerLastName;
    private String systemCaseId;
    private Date timestamp;

    public PDFShowHistoryDTO() {
    }

    public PDFShowHistoryDTO(int documentId,String documentName, String passengerFirstName, String passengerLastName, String systemCaseId, Date timestamp) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.passengerFirstName = passengerFirstName;
        this.passengerLastName = passengerLastName;
        this.systemCaseId = systemCaseId;
        this.timestamp = timestamp;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSystemCaseId() {
        return systemCaseId;
    }

    public void setSystemCaseId(String systemCaseId) {
        this.systemCaseId = systemCaseId;
    }
}
