package com.koyeb.rayshan.services;

import com.koyeb.rayshan.exception.ExpenseException;
import com.koyeb.rayshan.models.AuthData;
import com.koyeb.rayshan.models.LoginRequest;
import com.koyeb.rayshan.repositories.UserRepository;
import com.koyeb.rayshan.utils.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;
    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    public Mono<AuthData> authenticate(LoginRequest loginRequest) {
        log.info("Authenticating user: {}", loginRequest.getUserName());
        return userRepository.findByName(loginRequest.getUserName())
                .flatMap(user -> {
                    log.info("User found.");
                    String hashedPasswd = user.getPassword();
                    if (isCorrectPassword(loginRequest.getPassword(), hashedPasswd)) {
                        return Mono.just(user.getId());
                    } else {
                        log.info("Invalid credentials for username: {}", user.getName());
                        return Mono.error(new ExpenseException("Invalid credentials."));
                    }
                })
                .switchIfEmpty(Mono.error(new ExpenseException("User not found")))
                .flatMap(userId -> {
                    String token = jwtUtil.generateToken(loginRequest.getUserName());
                    AuthData authData = new AuthData();
                    authData.setToken(token);
                    authData.setUserId(userId);
                    authData.setUserName(loginRequest.getUserName());
                    //authData.getExpiry()
                    return Mono.just(authData);
                });
    }

    public String encrypt(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean isCorrectPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}
