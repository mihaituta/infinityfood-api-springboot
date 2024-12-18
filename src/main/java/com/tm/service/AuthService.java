package com.tm.service;

import com.tm.dto.UserLoginRequest;
import com.tm.dto.UserRegistrationRequest;
import com.tm.dto.UserResponseDTO;
import com.tm.exception.DuplicateResourceException;
import com.tm.exception.ResourceNotFoundException;
import com.tm.model.User;
import com.tm.repository.UserRepository;
import com.tm.security.CustomUserDetailsService;
import com.tm.security.JwtUtil;
import com.tm.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    // LOGIN
    public ResponseEntity<Response<Object>> login(UserLoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findUserByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            String jwtToken = jwtUtil.generateToken(authentication.getName());

            Map<String, Object> data = Map.of(
                    "jwt", jwtToken,
                    "roleId", user.getRoleId());

            return ResponseEntity.ok(new Response<>("success", "Logged-In successfully!", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("error", "Error Logging-In!", e.getMessage()));
        }
    }

    // REGISTER NEW USER (WILL NOT BE USED, JUST FOR TESTING FOR NOW)
    public String register(UserRegistrationRequest request) {
        if(userRepository.existsUserByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already in use!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoleId(request.getRole());

        userRepository.save(user);

        return "User registered successfully!";
    }

    // GET LOGGED-IN USER
    public UserResponseDTO getLoggedInUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) userDetailsService.loadUserByUsername(userDetails.getUsername());

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoleId().name()
        );
    }
}
