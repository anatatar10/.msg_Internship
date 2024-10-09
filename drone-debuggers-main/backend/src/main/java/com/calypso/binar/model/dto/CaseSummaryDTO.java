package com.calypso.binar.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
public class CaseSummaryDTO {
    private String systemCaseId;
    private Timestamp caseDate;
    private String flightNumber;
    private String firstPassengerName;
    private String lastPassengerName;
    private String status;
    private String assignedColleague;
    private ColleagueDTO assignedColleagueDTO;

    public CaseSummaryDTO() {
    }

    public CaseSummaryDTO(String systemCaseId, Timestamp caseDate, String flightNumber, String firstPassengerName, String lastPassengerName, String status, String assignedColleague, ColleagueDTO assignedColleagueDTO) {
        this.systemCaseId = systemCaseId;
        this.caseDate = caseDate;
        this.flightNumber = flightNumber;
        this.firstPassengerName = firstPassengerName;
        this.lastPassengerName = lastPassengerName;
        this.status = status;
        this.assignedColleague = assignedColleague;
        this.assignedColleagueDTO = assignedColleagueDTO;

    }
}
