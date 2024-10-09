package com.calypso.binar.model.dto;

import jakarta.validation.constraints.NotBlank;

public class DisruptionOptionDTO {
    @NotBlank
    private String disruptionOptionDescription;

    public DisruptionOptionDTO() {
    }

    public DisruptionOptionDTO( String disruptionOptionDescription) {
        this.disruptionOptionDescription = disruptionOptionDescription;
    }



    public String getDisruptionOptionDescription() {
        return disruptionOptionDescription;
    }

    public void setDisruptionOptionDescription(String disruptionOptionDescription) {
        this.disruptionOptionDescription = disruptionOptionDescription;
    }

    @Override
    public String toString() {
        return "DisruptionOptionDTO{" +
                ", disruptionOptionDescription='" + disruptionOptionDescription + '\'' +
                '}';
    }
}
