package com.mike.doctorapp.controller;

import com.mike.doctorapp.dto.auth.AuthRequest;
import com.mike.doctorapp.dto.auth.AuthResponse;
import com.mike.doctorapp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        String token = authService.authenticate(authRequest.getUsername(), authRequest.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

}
