package com.calypso.binar.model.dto;

import java.sql.Timestamp;
import java.util.Arrays;

public class GeneratedPdfDTO {
    private int documentId;
    private String documentName;
    private CaseDTO caseEntity;
    private byte[] data;
    private Timestamp uploadTimestamp;

    public GeneratedPdfDTO() {
    }

    public GeneratedPdfDTO(int documentId, String documentName, CaseDTO caseEntity, byte[] data, Timestamp uploadTimestamp) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.caseEntity = caseEntity;
        this.data = data;
        this.uploadTimestamp = uploadTimestamp;
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

    public CaseDTO getCaseEntity() {
        return caseEntity;
    }

    public void setCaseEntity(CaseDTO caseEntity) {
        this.caseEntity = caseEntity;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Timestamp getUploadTimestamp() {
        return uploadTimestamp;
    }

    public void setUploadTimestamp(Timestamp uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }

    @Override
    public String toString() {
        return "GeneratedPdfDTO{" +
                "documentId=" + documentId +
                ", documentName='" + documentName + '\'' +
                ", caseEntity=" + caseEntity +
                ", data=" + Arrays.toString(data) +
                ", uploadTimestamp=" + uploadTimestamp +
                '}';
    }
}
