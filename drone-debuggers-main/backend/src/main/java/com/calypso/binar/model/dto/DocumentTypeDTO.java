package com.calypso.binar.model.dto;

public class DocumentTypeDTO {

    private int documentTypeId;
    private String documentTypeName;

    public DocumentTypeDTO() {
    }

    public DocumentTypeDTO(int documentTypeId, String documentTypeName) {
        this.documentTypeId = documentTypeId;
        this.documentTypeName = documentTypeName;
    }

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

    @Override
    public String toString() {
        return "DocumentTypeDTO{" +
                "documentTypeId=" + documentTypeId +
                ", documentTypeName='" + documentTypeName + '\'' +
                '}';
    }
}
