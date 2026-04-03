package com.finance.finance_dashboard.service;

import com.finance.finance_dashboard.config.PasswordEncoder;
import com.finance.finance_dashboard.dto.request.LoginRequest;
import com.finance.finance_dashboard.dto.response.LoginResponse;
import com.finance.finance_dashboard.dto.request.RegisterRequest;
import com.finance.finance_dashboard.enums.Role;
import com.finance.finance_dashboard.exception.EmailAlreadyExistException;
import com.finance.finance_dashboard.exception.EmailNotFoundException;
import com.finance.finance_dashboard.jwt.JwtUtility;
import com.finance.finance_dashboard.model.User;
import com.finance.finance_dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtUtility jwtUtility;

    public String register(RegisterRequest registerRequest) {
        if(userRepository.findByActiveEmail(registerRequest.email()).isPresent()){
            throw new EmailAlreadyExistException("User with email as " + registerRequest.email() + " already exists");
        }
        Optional<User> userOpt = userRepository.findByDeactivateEmail(registerRequest.email());
        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setDeletedAt(null);
            user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(registerRequest.password()));
            userRepository.save(user);
            return "User Registration Successful";
        }
        else {
            User user = new User();
            user.setEmail(registerRequest.email());
            user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(registerRequest.password()));
            user.setName(registerRequest.name());
            Role userRole = switch (registerRequest.role().toUpperCase()){
                case "VIEWER" -> Role.VIEWER;
                case "ANALYST" -> Role.ANALYST;
                case "ADMIN" -> Role.ADMIN;
                default -> Role.VIEWER;
            };
            user.setRole(userRole);
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
            return "User Registration Successful";
        }
    }

    public LoginResponse login(LoginRequest loginRequest) {
        if(userRepository.findByActiveEmail(loginRequest.email()).isEmpty()){
            throw new EmailNotFoundException("Email as " + loginRequest.email() + " not found");
        }
        authenticate(loginRequest);
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.email());
        String token = jwtUtility.generateToken(userDetails);
        return new LoginResponse(loginRequest.email(), token);

    }

    public void authenticate(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        try{
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }
        catch(BadCredentialsException exception){
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
