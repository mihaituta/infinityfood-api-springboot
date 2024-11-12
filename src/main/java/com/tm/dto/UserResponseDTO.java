package com.tm.dto;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role_id;

    public UserResponseDTO(Long id, String name, String email, String role_id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role_id = role_id;
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

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }
}
