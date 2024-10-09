package com.calypso.binar.model.dto;

import com.calypso.binar.model.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
public class CaseDTO {
    private int caseId;
    @PastOrPresent
    private Timestamp dateCreated;
    @NotNull
    private Status status;
    @NotNull
    private ReservationSaveCaseDTO reservationData;
    private PassengerDetailsDTO passengerDetails;
    @NotNull
    private DisruptionDTO disruption;
    @NotNull
    private UserDTO passenger;
    private UserDTO colleague;
    @NotNull
    private List<FlightInfoDTO> flightsInfo;
    @NotNull
    private String systemCaseId;

    public CaseDTO() {
    }

    public CaseDTO(int caseId, Timestamp dateCreated, Status status, PassengerDetailsDTO passengerDetails, ReservationSaveCaseDTO reservationData, DisruptionDTO disruption, UserDTO passenger, UserDTO colleague, List<FlightInfoDTO> flightsInfo, String systemCaseId) {
        this.caseId = caseId;
        this.dateCreated = dateCreated;
        this.status = status;
        this.passengerDetails = passengerDetails;
        this.reservationData = reservationData;
        this.disruption = disruption;
        this.passenger = passenger;
        this.colleague = colleague;
        this.flightsInfo = flightsInfo;
        this.systemCaseId = systemCaseId;
    }

    @Override
    public String toString() {
        return "CaseDTO{" +
                "caseId=" + caseId +
                ", dateCreated=" + dateCreated +
                ", status=" + status +
                ", reservationSaveCaseData=" + reservationData +
                ", passengerDetails=" + passengerDetails +
                ", disruption=" + disruption +
                ", passenger=" + passenger +
                ", colleague=" + colleague +
                ", flightsInfo=" + flightsInfo +
                ", systemCaseId='" + systemCaseId + '\'' +
                '}';
    }
}
