package com.calypso.binar.model;

import jakarta.persistence.*;

@Entity
@Table(name = "document_type")
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_type_id")
    private int documentTypeId;

    @Column(name = "document_type_name", nullable = false)
    private String documentTypeName;

    // Default constructor
    public DocumentType() {}

    // Constructor with parameters
    public DocumentType(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    // Getters and Setters
    public int getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(int documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getDocumentTypeName() {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

    // toString method
    @Override
    public String toString() {
        return "DocumentType{" +
                "documentTypeId=" + documentTypeId +
                ", documentTypeName='" + documentTypeName + '\'' +
                '}';
    }
}
