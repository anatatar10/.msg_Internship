package com.calypso.binar.model.dto;

import jakarta.validation.constraints.NotBlank;

public class CancellationTypeDTO {

    @NotBlank
    private String cancellationTypeDescription;

    public CancellationTypeDTO() {
    }

    public CancellationTypeDTO( String cancellationTypeDescription) {
        this.cancellationTypeDescription = cancellationTypeDescription;
    }



    public String getCancellationTypeDescription() {
        return cancellationTypeDescription;
    }

    public void setCancellationTypeDescription(String cancellationTypeDescription) {
        this.cancellationTypeDescription = cancellationTypeDescription;
    }

    @Override
    public String toString() {
        return "CancellationTypeDTO{" +
                ", cancellationTypeDescription='" + cancellationTypeDescription + '\'' +
                '}';
    }
}
