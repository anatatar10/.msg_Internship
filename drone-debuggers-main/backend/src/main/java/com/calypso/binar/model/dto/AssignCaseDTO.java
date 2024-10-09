package com.calypso.binar.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignCaseDTO {
    private String systemCaseId;
    private String assignedColleagueEmail;


    public AssignCaseDTO() {
    }

    public AssignCaseDTO(String  systemCaseId, String assignedColleagueEmail) {
        this.systemCaseId = systemCaseId;
        this.assignedColleagueEmail = assignedColleagueEmail;

    }

    @Override
    public String toString() {
        return "AssignCaseDTO{" +
                "caseId=" + systemCaseId +
                ", assignedColleagueEmail='" + assignedColleagueEmail + '\'' +
                '}';
    }
}
