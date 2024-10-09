package com.calypso.binar.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;

@Getter
@Setter
public class DocumentDTO {

    private long lastModified;
    private Date lastModifiedDate;
    private String name;
    private long size;
    private String type;
    private String webkitRelativePath;
    private int documentId;
    private String documentType;
    private String documentName;
    private byte[] data;
    private Timestamp uploadTimestamp;

    public DocumentDTO() {
    }

    public DocumentDTO(long lastModified, Date lastModifiedDate, String name, long size, String type, String webkitRelativePath) {
        this.lastModified = lastModified;
        this.lastModifiedDate = lastModifiedDate;
        this.name = name;
        this.size = size;
        this.type = type;
        this.webkitRelativePath = webkitRelativePath;
    }

    public DocumentDTO(int documentId, String documentType, byte[] data, Timestamp uploadTimestamp) {
        this.documentId = documentId;
        this.documentType = documentType;
        this.documentName = documentName;
        this.data = data;
        this.uploadTimestamp = uploadTimestamp;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Timestamp getUploadTimestamp() {
        return uploadTimestamp;
    }

    public void setUploadTimestamp(Timestamp uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }

    @Override
    public String toString() {
        return "DocumentDTO{" +
                "documentId=" + documentId +
                ", documentType='" + documentType + '\'' +
                ", documentName='" + documentName + '\'' +
                ", data=" + Arrays.toString(data) +
                ", uploadTimestamp=" + uploadTimestamp +
                '}';
    }
}
