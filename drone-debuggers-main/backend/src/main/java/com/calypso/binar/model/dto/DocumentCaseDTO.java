package com.calypso.binar.model.dto;

import java.time.LocalDateTime;

public class DocumentCaseDTO {

    private int documentId;

    private String documentType;

    private byte[] fileData;

    private String fileName;

    private String fileType;

    private LocalDateTime uploadDate;

    public DocumentCaseDTO(int documentId,String documentType, byte[] fileData, String fileName, String fileType, LocalDateTime uploadDate) {
        this.documentId = documentId;
        this.documentType = documentType;
        this.fileData = fileData;
        this.fileName = fileName;
        this.fileType = fileType;
        this.uploadDate = uploadDate;
    }

    public DocumentCaseDTO() {

    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
}
