package com.calypso.binar.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "disruption_option")
public class DisruptionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disruption_option_id")
    private int disruptionOptionId;

    @Column(name = "disruption_option_description", nullable = false)
    private String disruptionOptionDescription;

    // Default constructor
    public DisruptionOption() {}

    // Constructor with parameters
    public DisruptionOption(String disruptionOptionDescription) {
        this.disruptionOptionDescription = disruptionOptionDescription;
    }

    // Getters and Setters
    public int getDisruptionOptionId() {
        return disruptionOptionId;
    }

    public void setDisruptionOptionId(int disruptionOptionId) {
        this.disruptionOptionId = disruptionOptionId;
    }

    public String getDisruptionOptionDescription() {
        return disruptionOptionDescription;
    }

    public void setDisruptionOptionDescription(String disruptionOptionDescription) {
        this.disruptionOptionDescription = disruptionOptionDescription;
    }

    // toString method
    @Override
    public String toString() {
        return "DisruptionOption{" +
                "disruptionOptionId=" + disruptionOptionId +
                ", disruptionOptionDescription='" + disruptionOptionDescription + '\'' +
                '}';
    }
}
