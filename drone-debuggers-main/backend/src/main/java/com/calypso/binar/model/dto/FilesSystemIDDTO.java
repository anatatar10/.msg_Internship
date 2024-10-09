package com.calypso.binar.model.dto;

import java.util.Arrays;

public class FilesSystemIDDTO {
    private String systemCaseId;
    private AttachedFilesDTO[] attachments;

    public FilesSystemIDDTO(String systemCaseId, AttachedFilesDTO[] attachments) {
        this.systemCaseId = systemCaseId;
        this.attachments = attachments;
    }

    public String getSystemCaseId() {
        return systemCaseId;
    }

    public void setSystemCaseId(String systemCaseId) {
        this.systemCaseId = systemCaseId;
    }

    public AttachedFilesDTO[] getAttachedFiles() {
        return attachments;
    }

    public void setAttachments(AttachedFilesDTO[] attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "FilesSystemIDDTO{" +
                "systemCaseId='" + systemCaseId + '\'' +
                ", attachments=" + Arrays.toString(attachments) +
                '}';
    }
}
