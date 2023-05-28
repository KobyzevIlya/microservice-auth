package ru.hse.hw4.auth.responses;

import lombok.Getter;
import lombok.Setter;
import ru.hse.hw4.auth.services.AuthStatus;

@Getter
@Setter
public class RegistrationResponse {

    private AuthStatus status;
    private String message;

}