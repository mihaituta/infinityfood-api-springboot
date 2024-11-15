package com.tm.dto;

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

    private String roleId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRoleId() { return roleId; }

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
