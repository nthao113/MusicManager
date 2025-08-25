package com.example.demo.Security;

import com.example.demo.Security.Jwt.JwtResponse;
import com.example.demo.Security.Jwt.JwtUtil;
import com.example.demo.Model.Role;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Security.Jwt.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@Tag(name = "Authentication Controller", description = "Handles user registration and login")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authManager;

    @Operation(summary = "Register new user", description = "Create a new user with default role USER")
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody(
                    description = "User object to register",
                    required = true,
                    content = @Content(schema = @Schema(implementation = User.class))
            )
            @org.springframework.web.bind.annotation.RequestBody User user) {

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @Operation(summary = "Login and get JWT token", description = "Authenticate user and return JWT access token")
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody(
                    description = "User login request with username and password",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody LoginRequest loginRequest) {

        System.out.println("Login: " + loginRequest.getUsername() + ", " + loginRequest.getPassword());

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().toString());

        return ResponseEntity.ok(new JwtResponse(token));
    }
}

