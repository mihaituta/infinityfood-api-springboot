package com.tm.service;

import com.tm.dto.UserCreateDTO;
import com.tm.dto.UserResponseDTO;
import com.tm.model.User;
import com.tm.repository.UserRepository;
import com.tm.security.Role;
import com.tm.util.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // GET all users
    public Map<String, Object> getAllUsers() {
        List<UserResponseDTO> users = userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRoleId().name() // Role as a string (Admin or Staff)
                ))
                .toList();

        return Map.of(
                "responseType", "success",
                "data", users
        );
    }

    // CREATE a new user
    public ResponseEntity<Response<UserResponseDTO>> createUser(UserCreateDTO userCreateDTO) {
        try {
            if(userRepository.existsUserByEmail(userCreateDTO.getEmail())) {
                return ResponseEntity.ok(new Response<>("error", "Email already registered", "uniqueEmail"));
            }

            if(userRepository.existsUserByName(userCreateDTO.getName())) {
                return ResponseEntity.ok(new Response<>("error", "Username taken", "uniqueName"));
            }

            User user = new User();
            user.setName(userCreateDTO.getName());
            user.setEmail(userCreateDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
            user.setRoleId(Role.valueOf(userCreateDTO.getRoleId()));

            userRepository.save(user);

            UserResponseDTO userResponse = new UserResponseDTO(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRoleId().name()
            );

            return ResponseEntity.ok(new Response<>("success", "User created successfully", userResponse));
        } catch (DataIntegrityViolationException e) {
            // Handle unique constraint violation
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Response<>("error", "Email or Username already registered", "uniqueViolation"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Failed to create user: ", e.getMessage()));
        }
    }

    // UPDATE user
    public ResponseEntity<Response<UserResponseDTO>> updateUser(Integer id, UserCreateDTO userCreateDTO) {
        Optional<User> userOpt = userRepository.findById(id);
        if(userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response<>("error", "User not found", "userNotFound"));
        }

        if(userRepository.existsUserByEmail(userCreateDTO.getEmail())) {
            return ResponseEntity.ok(new Response<>("error", "Email already registered", "uniqueEmail"));
        }

        if(userRepository.existsUserByName(userCreateDTO.getName())) {
            return ResponseEntity.ok(new Response<>("error", "Username taken", "uniqueName"));
        }

        User user = userOpt.get();
        if(userCreateDTO.getName() != null)
            user.setName(userCreateDTO.getName());
        if(userCreateDTO.getEmail() != null)
            user.setEmail(userCreateDTO.getEmail());
        if(userCreateDTO.getPassword() != null)
            user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        if(userCreateDTO.getRoleId() != null)
            user.setRoleId(Role.valueOf(userCreateDTO.getRoleId()));

        userRepository.save(user);

        UserResponseDTO userResponse = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoleId().name()
        );

        return ResponseEntity.ok(new Response<>("success", "User updated successfully", userResponse));
    }

    // DELETE user
    public ResponseEntity<Response<String>> deleteUser(Integer id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(new Response<>("success", "User deleted successfully"));
    }
}
