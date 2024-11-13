package com.tm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCreateDTO {
    private Long id;
    private String name;
    private String email;
    private String password;

    @JsonProperty("role_id")
    private String roleId;

    public UserCreateDTO(Long id, String name, String email, String password, String roleId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
