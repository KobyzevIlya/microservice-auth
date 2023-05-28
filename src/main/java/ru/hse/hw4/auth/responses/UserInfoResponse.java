package ru.hse.hw4.auth.responses;

import lombok.Getter;
import lombok.Setter;
import ru.hse.hw4.auth.services.AuthStatus;

@Getter
@Setter
public class UserInfoResponse {

    private AuthStatus status;
    private String message;
    
    private String username;
    private String email;
    private String role;
}
