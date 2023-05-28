package ru.hse.hw4.auth.responses;

import lombok.Getter;
import lombok.Setter;
import ru.hse.hw4.auth.services.AuthStatus;

@Getter
@Setter
public class LoginResponse {

    private String token;
    private AuthStatus status;
    private String message;

    public LoginResponse(String token) {
        this.token = token;
    }
}