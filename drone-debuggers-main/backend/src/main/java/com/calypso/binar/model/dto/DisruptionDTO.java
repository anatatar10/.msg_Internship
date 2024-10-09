package com.calypso.binar.model.dto;

import jakarta.validation.constraints.NotNull;

public class DisruptionDTO {
    @NotNull
    private CancellationTypeDTO cancellationType;
    private DisruptionOptionDTO disruptionOption;
    private AirlineMotiveDTO airlineMotive;
    private String incidentDescription;

    public DisruptionDTO() {
    }

    public DisruptionDTO( CancellationTypeDTO cancellationType, DisruptionOptionDTO disruptionOption, AirlineMotiveDTO airlineMotive, String incidentDescription) {
        this.cancellationType = cancellationType;
        this.disruptionOption = disruptionOption;
        this.airlineMotive = airlineMotive;
        this.incidentDescription = incidentDescription;
    }



    public CancellationTypeDTO getCancellationType() {
        return cancellationType;
    }

    public void setCancellationType(CancellationTypeDTO cancellationType) {
        this.cancellationType = cancellationType;
    }

    public DisruptionOptionDTO getDisruptionOption() {
        return disruptionOption;
    }

    public void setDisruptionOption(DisruptionOptionDTO disruptionOption) {
        this.disruptionOption = disruptionOption;
    }

    public AirlineMotiveDTO getAirlineMotive() {
        return airlineMotive;
    }

    public void setAirlineMotive(AirlineMotiveDTO airlineMotive) {
        this.airlineMotive = airlineMotive;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    @Override
    public String toString() {
        return "DisruptionDTO{" +
                ", cancellationType=" + cancellationType +
                ", disruptionOption=" + disruptionOption +
                ", airlineMotive=" + airlineMotive +
                ", incidentDescription='" + incidentDescription + '\'' +
                '}';
    }
}
