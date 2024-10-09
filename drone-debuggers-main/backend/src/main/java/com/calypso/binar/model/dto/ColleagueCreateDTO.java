package com.calypso.binar.model.dto;

public class ColleagueCreateDTO extends PassengerCreateDTO{
    private String password;

    public ColleagueCreateDTO() {
    }

    public ColleagueCreateDTO(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
