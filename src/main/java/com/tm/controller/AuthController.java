package com.tm.controller;

import com.tm.dto.UserLoginRequest;
import com.tm.dto.UserRegistrationRequest;
import com.tm.dto.UserResponseDTO;
import com.tm.model.User;
import com.tm.repository.UserRepository;
import com.tm.security.CustomUserDetailsService;
import com.tm.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final CustomUserDetailsService userDetailsService;

    public AuthController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@Valid @RequestBody UserLoginRequest loginRequest) {
        System.out.println("Searching for user with email: " + loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        String jwtToken = jwtUtil.generateToken(authentication.getName());

        Map<String, Object> response = new HashMap<>();
        response.put("responseType", "success"); // Include responseType to match Vue logic
        response.put("jwt", jwtToken);
        response.put("role_id", user.getRole());
//        response.put("data", new HashMap<String, Object>() {{
//            put("jwt", jwtToken);
//            put("role_id", user.getRole());
//        }});

        return ResponseEntity.ok(response);
    }

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        // Check if the email is already in use
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }
        System.out.println(request.getRole());
        // Create a new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole()); // ADMIN or STAFF based on the request

        // Save user to the database
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponseDTO> getLoggedInUser() {
        // Get the currently authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Get the user entity based on the authenticated user's username (email)
        User user = (User) userDetailsService.loadUserByUsername(userDetails.getUsername());

        return ResponseEntity.ok(new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        ));
    }
}
