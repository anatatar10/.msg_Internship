package com.calypso.binar.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AttachedFilesDTO {
    @NotBlank
    private String documentType;
    @NotBlank
    private String fileName;
    @NotBlank
    private String fileType;
    @NotNull
    private byte[] fileData;
    @PastOrPresent
    private LocalDateTime uploadDate;

    public AttachedFilesDTO(String documentType, String fileName, byte[] fileData, String fileType, LocalDateTime uploadDate) {
        this.documentType = documentType;
        this.fileName = fileName;
        this.fileData = fileData;
        this.fileType = fileType;
        this.uploadDate = uploadDate;
    }

    public AttachedFilesDTO() {
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "AttachedFilesDTO{" +
                "documentType='" + documentType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileData=" + Arrays.toString(fileData) +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
