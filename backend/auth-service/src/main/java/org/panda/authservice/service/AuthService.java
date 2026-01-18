package org.panda.authservice.service;

import lombok.RequiredArgsConstructor;
import org.panda.authservice.dto.LoginRequest;
import org.panda.authservice.dto.RegisterRequest;
import org.panda.authservice.entity.Role;
import org.panda.authservice.entity.User;
import org.panda.authservice.exception.InvalidCredentialsException;
import org.panda.authservice.exception.UserAlreadyExistsException;
import org.panda.authservice.exception.UserNotFoundException;
import org.panda.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public void register(RegisterRequest req) {
        if(repo.findByUsername(req.getUsername()).isPresent()) throw new UserAlreadyExistsException("Username already exists");
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(Role.USER);
        repo.save(user);
    }

    public String login(LoginRequest req) {
        User user = repo.findByUsername(req.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!encoder.matches(req.getPassword(), user.getPassword())) throw new InvalidCredentialsException("Invalid credentials");
        return jwtService.generateToken(user);
    }
}
