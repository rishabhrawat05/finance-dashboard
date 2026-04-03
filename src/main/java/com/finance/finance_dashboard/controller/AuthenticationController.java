package com.finance.finance_dashboard.controller;

import com.finance.finance_dashboard.dto.request.LoginRequest;
import com.finance.finance_dashboard.dto.response.LoginResponse;
import com.finance.finance_dashboard.dto.request.RegisterRequest;
import com.finance.finance_dashboard.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API for Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and returns the resource location"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest){
        return ResponseEntity.created(URI.create("/register")).body(authenticationService.register(registerRequest));
    }

    @Operation(
            summary = "Login user",
            description = "Authenticates user and returns JWT token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.created(URI.create("/login")).body(authenticationService.login(loginRequest));
    }
}
