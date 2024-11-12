package com.tm.controller;

import com.tm.dto.UserCreateDTO;
import com.tm.dto.UserResponseDTO;
import com.tm.model.User;
import com.tm.repository.UserRepository;
import com.tm.security.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('Admin')") // Ensure only admins can access these endpoints
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // GET all users
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> users = userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole().name() // Role as a string (Admin or Staff)
                ))
                .collect(Collectors.toList());


        return ResponseEntity.ok(users);
    }

    // CREATE a new user
    @PostMapping("/user")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        User user = new User();
        user.setName(userCreateDTO.getName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        user.setRole(Role.valueOf(userCreateDTO.getRole_id()));

        userRepository.save(user);

        // Map to UserResponseDTO for a consistent response structure
        UserResponseDTO userResponse = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("responseType", "success"); // Include responseType to match Vue logic
        response.put("data", userResponse);


        return ResponseEntity.ok(response);
        //return ResponseEntity.ok(userResponse);
    }

    // UPDATE user
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserCreateDTO userCreateDTO) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOpt.get();
        if (userCreateDTO.getName() != null) user.setName(userCreateDTO.getName());
        if (userCreateDTO.getEmail() != null) user.setEmail(userCreateDTO.getEmail());
        if (userCreateDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        if (userCreateDTO.getRole_id() != null) user.setRole(Role.valueOf(userCreateDTO.getRole_id()));

        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    // DELETE user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
