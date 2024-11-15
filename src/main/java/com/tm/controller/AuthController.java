package com.tm.controller;

import com.tm.dto.UserLoginRequest;
import com.tm.dto.UserRegistrationRequest;
import com.tm.dto.UserResponseDTO;
import com.tm.service.AuthService;
import com.tm.util.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Response<Object>> loginUser(@Valid @RequestBody UserLoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        String responseMessage = authService.register(request);
        return ResponseEntity.ok(responseMessage);
    }

    // Get logged-in user info endpoint
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getLoggedInUser() {
        UserResponseDTO userResponse = authService.getLoggedInUser();

        Map<String, Object> response = new HashMap<>();
        response.put("responseType", "success");
        response.put("data", userResponse);

        return ResponseEntity.ok(response);
    }
}
