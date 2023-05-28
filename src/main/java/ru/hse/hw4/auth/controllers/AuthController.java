package ru.hse.hw4.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.hse.hw4.auth.requests.LoginRequest;
import ru.hse.hw4.auth.requests.RegistrationRequest;
import ru.hse.hw4.auth.responses.LoginResponse;
import ru.hse.hw4.auth.responses.RegistrationResponse;
import ru.hse.hw4.auth.responses.UserInfoResponse;
import ru.hse.hw4.auth.services.AuthService;
import ru.hse.hw4.auth.services.AuthStatus;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerUser(@RequestBody RegistrationRequest request) {
        RegistrationResponse response = authService.registerUser(request);
        if (response.getStatus() == AuthStatus.CORRECT) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if (response.getStatus() == AuthStatus.INVALID) {
            return ResponseEntity.unprocessableEntity().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
        LoginResponse response = authService.loginUser(request);
        if (response.getStatus() == AuthStatus.CORRECT) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if (response.getStatus() == AuthStatus.INVALID) {
            return ResponseEntity.unprocessableEntity().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestParam("token") String token) {
        if (token == null) {
            return ResponseEntity.badRequest().body(new UserInfoResponse());
        }
        
        UserInfoResponse response = authService.getUserInfo(token);
        if (response.getStatus() == AuthStatus.CORRECT) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else if (response.getStatus() == AuthStatus.INVALID) {
            return ResponseEntity.unprocessableEntity().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
