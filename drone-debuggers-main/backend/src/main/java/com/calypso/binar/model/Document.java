package com.calypso.binar.model;

import java.sql.Timestamp;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private int documentId;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseEntity;

    @ManyToOne
    @JoinColumn(name = "document_type")
    private DocumentType documentType;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_data_type")
    private String documentDataType;

    @Lob
    @Column(name = "data", columnDefinition = "MEDIUMBLOB")
    private byte[] data;

    @Column(name = "upload_timestamp")
    private LocalDateTime uploadTimestamp;

    // Default constructor
    public Document() {}

    public Document(int documentId, Case caseEntity, String documentDataType, DocumentType documentType, String documentName, LocalDateTime uploadTimestamp, byte[] data) {
        this.documentId = documentId;
        this.caseEntity = caseEntity;
        this.documentDataType = documentDataType;
        this.documentType = documentType;
        this.documentName = documentName;
        this.uploadTimestamp = uploadTimestamp;
        this.data = data;
    }

    public Document(Case caseEntity, DocumentType documentType, String documentName, String documentDataType, byte[] data, LocalDateTime uploadTimestamp) {
        this.caseEntity = caseEntity;
        this.documentType = documentType;
        this.documentName = documentName;
        this.documentDataType = documentDataType;
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

    public Case getCaseEntity() {
        return caseEntity;
    }

    public void setCaseEntity(Case caseEntity) {
        this.caseEntity = caseEntity;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public LocalDateTime getUploadTimestamp() {
        return uploadTimestamp;
    }

    public void setUploadTimestamp(LocalDateTime uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }

    public String getDocumentDataType() {
        return documentDataType;
    }

    public void setDocumentDataType(String documentDataType) {
        this.documentDataType = documentDataType;
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentId=" + documentId +
                ", caseEntity=" + caseEntity.getSystemCaseId() +
                ", documentType=" + documentType +
                ", documentName='" + documentName + '\'' +
                ", documentDataType='" + documentDataType + '\'' +
                ", uploadTimestamp=" + uploadTimestamp +
                '}';
    }
}
