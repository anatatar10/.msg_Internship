package com.calypso.binar.model.dto;

import jakarta.validation.constraints.NotBlank;

public class UserDTO {

    private Integer userId;
    @NotBlank
    private String email;
    private boolean firstLogin;
    private boolean accountBlocked;
    private Integer numberOfFailedAttempts;
    private RoleDTO role; // DTO pentru rol

    public UserDTO() {}

    public UserDTO(Integer userId, String email, boolean firstLogin, boolean accountBlocked, Integer numberOfFailedAttempts, RoleDTO role) {
        this.userId = userId;
        this.email = email;
        this.firstLogin = firstLogin;
        this.accountBlocked = accountBlocked;
        this.numberOfFailedAttempts = numberOfFailedAttempts;
        this.role = role;
    }

    // Getters și Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public boolean isAccountBlocked() {
        return accountBlocked;
    }

    public void setAccountBlocked(boolean accountBlocked) {
        this.accountBlocked = accountBlocked;
    }

    public Integer getNumberOfFailedAttempts() {
        return numberOfFailedAttempts;
    }

    public void setNumberOfFailedAttempts(Integer numberOfFailedAttempts) {
        this.numberOfFailedAttempts = numberOfFailedAttempts;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", firstLogin=" + firstLogin +
                ", accountBlocked=" + accountBlocked +
                ", numberOfFailedAttempts=" + numberOfFailedAttempts +
                ", role=" + role +
                '}';
    }
}
