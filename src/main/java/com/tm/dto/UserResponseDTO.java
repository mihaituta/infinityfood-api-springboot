package com.tm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;

    @JsonProperty("role_id") // to keep it role_id when retrieved
    private String roleId;

    // No-argument constructor
    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserResponseDTO(Long id, String name, String email, String roleId) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
