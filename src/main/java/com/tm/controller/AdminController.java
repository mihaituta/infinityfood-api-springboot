package com.tm.controller;

import com.tm.dto.UserCreateDTO;
import com.tm.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('Admin')") // Ensure only admins can access these endpoints
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // GET all users
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    // CREATE a new user
    @PostMapping("/user")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        return adminService.createUser(userCreateDTO);
    }

    // UPDATE user
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserCreateDTO userCreateDTO) {
        return adminService.updateUser(id, userCreateDTO);
    }

    // DELETE user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        return adminService.deleteUser(id);
    }
}