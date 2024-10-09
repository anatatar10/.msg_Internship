package com.calypso.binar.model.dto;

public class ColleagueDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    public ColleagueDTO() {
    }

    public ColleagueDTO(String firstName, String lastName, String email, String role ) {
        this.lastName = lastName;
        this.email = email;
        this.firstName = firstName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}

    @Override
    public String toString() {
        return "UserCaselistDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
