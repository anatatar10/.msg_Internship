package com.calypso.binar;

import com.calypso.binar.model.Role;

import java.util.Set;

public class SignInResponse {

    private String token;

    private Set<Role> roles;

    private Boolean firstLogIn;

    public SignInResponse(String token, Set<Role> roles, Boolean firstLogIn) {
        this.token = token;
        this.roles = roles;
        this.firstLogIn = firstLogIn;
    }

    public Boolean getFirstLogIn() {
        return firstLogIn;
    }

    public void setFirstLogIn(Boolean firstLogIn) {
        this.firstLogIn = firstLogIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
