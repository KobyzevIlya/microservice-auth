package ru.hse.hw4.auth.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.hse.hw4.auth.domain.Session;
import ru.hse.hw4.auth.domain.User;
import ru.hse.hw4.auth.repos.SessionRepository;
import ru.hse.hw4.auth.repos.UserRepository;
import ru.hse.hw4.auth.requests.LoginRequest;
import ru.hse.hw4.auth.requests.RegistrationRequest;
import ru.hse.hw4.auth.responses.LoginResponse;
import ru.hse.hw4.auth.responses.RegistrationResponse;
import ru.hse.hw4.auth.responses.UserInfoResponse;
import ru.hse.hw4.auth.security.Jwt;
import ru.hse.hw4.auth.security.PasswordEncrypter;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final Jwt jwt;
    private final PasswordEncrypter passwordEncrypter;

    @Autowired
    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, Jwt jwt, PasswordEncrypter passwordEncrypter) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.jwt = jwt;
        this.passwordEncrypter = passwordEncrypter;
    }

    public RegistrationResponse registerUser(RegistrationRequest request) {
        RegistrationResponse response = new RegistrationResponse();
        if (!request.getRole().equals("chef") && !request.getRole().equals("customer") && !request.getRole().equals("manager")) {
            response.setStatus(AuthStatus.INVALID);
            response.setMessage("Invalid role");
            return response;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(request.getEmail()).matches()) {
            response.setStatus(AuthStatus.INVALID);
            response.setMessage("Invalid email style");
            return response;
        }

        User searchResult = userRepository.findByEmail(request.getEmail().toLowerCase());
        if (!(searchResult == null)) {
            response.setStatus(AuthStatus.INVALID);
            response.setMessage("Email already in use");
            return response;
        }

        searchResult = userRepository.findByUsername(request.getUsername());
        if (!(searchResult == null)) {
            response.setStatus(AuthStatus.INVALID);
            response.setMessage("Username already in use");
            return response;
        }


        User newUser = new User();
        newUser.setEmail(request.getEmail().toLowerCase());
        newUser.setUsername(request.getUsername());
        newUser.setPasswrd(passwordEncrypter.encryptPassword(request.getPassword()));
        newUser.setRole(request.getRole());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        try {
            userRepository.save(newUser);
            response.setStatus(AuthStatus.CORRECT);
            return response;
        } catch (Exception e) {
            response.setStatus(AuthStatus.UNEXPECTED);
            response.setMessage("Unexpected error");
            return response;
        }
    }

    public LoginResponse loginUser(LoginRequest request) {
        LoginResponse response = new LoginResponse(null);
        User searchResult = userRepository.findByEmailAndPasswrd(request.getEmail().toLowerCase(), passwordEncrypter.encryptPassword(request.getPassword()));
        if (searchResult == null) {
            response.setStatus(AuthStatus.INVALID);
            response.setMessage("Invalid email or password");
            return response;
        }


        try {
            String token = jwt.createJwt(searchResult.getEmail(), searchResult.getId());
            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);
            createSession(searchResult.getId(), token, expiresAt);

            response.setToken(token);
            response.setStatus(AuthStatus.CORRECT);
            return response;
        } catch (Exception e) {
            response.setStatus(AuthStatus.UNEXPECTED);
            response.setMessage("Unexpected exeption");
            return response;
        }
    }

    private void createSession(Long userId, String token, LocalDateTime expiresAt) {
        Session session = new Session();
        session.setUser_id(userId);
        session.setToken(token);
        session.setExpiresAt(expiresAt);

        sessionRepository.save(session);
    }

    public UserInfoResponse getUserInfo(String token) {
        UserInfoResponse response = new UserInfoResponse();

        try {
            Session searchResult = sessionRepository.findByToken(token);
            if (searchResult == null) {
                response.setStatus(AuthStatus.INVALID);
                response.setMessage("No such token");
                return response;
            }
            Optional<User> userOptional = userRepository.findById(searchResult.getUser_id());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                
                response.setStatus(AuthStatus.CORRECT);
                response.setUsername(user.getUsername());
                response.setEmail(user.getEmail());
                response.setRole(user.getRole());
            } else {
                response.setStatus(AuthStatus.INVALID);
                response.setMessage("No such user");
            }
            return response;
        } catch (Exception e) {
            response.setStatus(AuthStatus.UNEXPECTED);
            response.setMessage("Unexpected exeption");
            return response;
        }
    }
}
