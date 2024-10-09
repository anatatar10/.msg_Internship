package com.calypso.binar.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class PassengerDetailsDTO {
    private int passengerDetailsId;
    private String firstName;
    private String lastName;
    @NotNull
    @PastOrPresent
    private Date dateOfBirth;
    @NotNull
    @Pattern(regexp = "^\\d{10}$")
    private String phoneNumber;
    @NotNull
    private String address;
    @NotNull
    @Pattern(regexp = "^\\d{2,}$")
    private int postalCode;
    private String email;

    public PassengerDetailsDTO() {
    }

    public PassengerDetailsDTO(int passengerDetailsId, String firstName, String lastName, Date dateOfBirth, String phoneNumber, String address, int postalCode, String email) {
        this.passengerDetailsId = passengerDetailsId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.postalCode = postalCode;
        this.email = email;
    }

    @Override
    public String toString() {
        return "PassengerDetailsDTO{" +
                "passengerDetailsId=" + passengerDetailsId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", postalCode=" + postalCode +
                ", email='" + email +
                '}';
    }
}
