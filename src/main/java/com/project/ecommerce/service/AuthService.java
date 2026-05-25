package com.project.ecommerce.service;


import com.project.ecommerce.dto.JwtResponse;
import com.project.ecommerce.dto.LoginRequest;
import com.project.ecommerce.dto.RegisterRequest;
import com.project.ecommerce.entity.Role;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.exception.UserAlreadyExistsException;
import com.project.ecommerce.repository.UserRepository;
import com.project.ecommerce.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;


    public String register(RegisterRequest request) {

        log.info("Creating user: {}", request.getEmail());
        if(userRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            log.error("User already exists with email {}", request.getEmail());
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);

        userRepository.save(user);

        log.info("User created: {}", user.getEmail());
        return "User registered successfully";
    }

    public JwtResponse login(LoginRequest request) {

        authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        String token = jwtUtil.generateToken(request.getEmail());

        log.info("Jwt token generated for email: {}", request.getEmail());
        return new JwtResponse(token);
    }
}