package com.calypso.binar.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "cancellation_type")
public class CancellationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cancellation_type_id")
    private int cancellationTypeId;

    @Column(name = "cancellation_type_description", nullable = false)
    private String cancellationTypeDescription;

    // Default constructor
    public CancellationType() {}

    // Constructor with parameters
    public CancellationType(String cancellationTypeDescription) {
        this.cancellationTypeDescription = cancellationTypeDescription;
    }

    // Getters and Setters
    public int getCancellationTypeId() {
        return cancellationTypeId;
    }

    public void setCancellationTypeId(int cancellationTypeId) {
        this.cancellationTypeId = cancellationTypeId;
    }

    public String getCancellationTypeDescription() {
        return cancellationTypeDescription;
    }

    public void setCancellationTypeDescription(String cancellationTypeDescription) {
        this.cancellationTypeDescription = cancellationTypeDescription;
    }

    // toString method
    @Override
    public String toString() {
        return "CancellationType{" +
                "cancellationTypeId=" + cancellationTypeId +
                ", cancellationTypeDescription='" + cancellationTypeDescription + '\'' +
                '}';
    }
}
