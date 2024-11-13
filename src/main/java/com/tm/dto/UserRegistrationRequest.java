package com.tm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tm.security.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;


public class UserRegistrationRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private Role role;

    //private Integer role_id;
    @JsonProperty("role_id")
    private String roleId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    //public Integer getRole_id() { return role_id; }
    public String getRoleId() { return roleId; }

    // Set role_id and map to the Role enum
   /* public void setRole_id(Integer role_id) {
        if (role_id != 0 && role_id != 1) {
            throw new IllegalArgumentException("Invalid role_id: must be 0 (Admin) or 1 (Staff)");
        }
        this.role = role_id == 0 ? Role.Admin : Role.Staff;
        this.role_id = role_id;
    }*/
    public void setRoleId(String roleId) {
        if (!Objects.equals(roleId, "Admin") && !Objects.equals(roleId, "Staff")) {
            throw new IllegalArgumentException("Invalid roleId: must be Admin or Staff");
        }
        this.role = roleId.equals("Admin") ? Role.Admin : Role.Staff;
        this.roleId = roleId;
    }

    public Role getRole() { return role; }
   // public void setRole(Role role) { this.role = role; }
}
