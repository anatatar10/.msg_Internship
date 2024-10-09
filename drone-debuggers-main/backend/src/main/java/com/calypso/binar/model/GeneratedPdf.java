package com.calypso.binar.model;

import java.sql.Timestamp;


import jakarta.persistence.*;

import java.util.Date;
@Entity
@Table(name = "generated_pdf")
public class GeneratedPdf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private int documentId;

    @Column(name = "document_name", nullable = false)
    private String documentName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @Lob
    @Column(name = "data", columnDefinition = "MEDIUMBLOB")
    private byte[] data;


    @Column(name = "upload_timestamp")
    private Timestamp uploadTimestamp;

    // Default constructor
    public GeneratedPdf() {
    }

    // Constructor with parameters
    public GeneratedPdf(String documentName, Case caseEntity, byte[] data, Timestamp uploadTimestamp) {
        this.documentName = documentName;
        this.caseEntity = caseEntity;
        this.data = data;
        this.uploadTimestamp = uploadTimestamp;
    }

    // Getters and Setters
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

    public Case getCaseEntity() {
        return caseEntity;
    }

    public void setCaseEntity(Case caseEntity) {
        this.caseEntity = caseEntity;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Date getUploadTimestamp() {
        return uploadTimestamp;
    }

    public void setUploadTimestamp(Timestamp uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }
}
