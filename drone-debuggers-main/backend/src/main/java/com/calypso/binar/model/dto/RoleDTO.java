package com.calypso.binar.model.dto;

public class RoleDTO {

    private Integer roleId;
    private String roleName;

    // Constructori
    public RoleDTO() {}

    public RoleDTO(Integer roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    // Getters și Setters
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
