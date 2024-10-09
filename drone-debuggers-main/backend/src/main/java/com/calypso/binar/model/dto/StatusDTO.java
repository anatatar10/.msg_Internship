package com.calypso.binar.model.dto;

public class StatusDTO {
    private int statusID;
    private String statusName;

    public StatusDTO() {
    }

    public StatusDTO(int statusID, String statusName) {
        this.statusID = statusID;
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    @Override
    public String toString() {
        return "StatusDTO{" +
                "statusID=" + statusID +
                ", statusName='" + statusName + '\'' +
                '}';
    }

}
