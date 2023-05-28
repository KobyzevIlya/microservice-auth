package ru.hse.hw4.auth.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

    private String username;
    private String email;
    private String password;
    private String role;
}

