package com.calypso.binar.model.dto;

public class UserListDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String roleName;
    private Long noAssignedCases;

    public UserListDTO(String firstName, String lastName, String email, String roleName, Long noAssignedCases) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roleName = roleName;
        this.noAssignedCases = noAssignedCases;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getNoAssignedCases() {
        return noAssignedCases;
    }

    public void setNoAssignedCases(Long noAssignedCases) {
        this.noAssignedCases = noAssignedCases;
    }
}
