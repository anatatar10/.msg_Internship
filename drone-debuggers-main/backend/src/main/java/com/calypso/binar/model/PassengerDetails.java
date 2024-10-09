package com.calypso.binar.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "passenger_details")
public class PassengerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_details_id")
    private int passengerDetailsId;

    @Column(name = "date_of_birth", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "postal_code", nullable = false)
    private int postalCode;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    // Default constructor
    public PassengerDetails() {}

    // Constructor with parameters
    public PassengerDetails(Date dateOfBirth, String phone, String address, int postalCode, String email, String firstName, String lastName) {
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.address = address;
        this.postalCode = postalCode;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and Setters
    public int getPassengerDetailsId() {
        return passengerDetailsId;
    }

    public void setPassengerDetailsId(int passengerDetailsId) {
        this.passengerDetailsId = passengerDetailsId;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // toString method

    @Override
    public String toString() {
        return "PassengerDetails{" +
                "passengerDetailsId=" + passengerDetailsId +
                ", dateOfBirth=" + dateOfBirth +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", postalCode=" + postalCode +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

