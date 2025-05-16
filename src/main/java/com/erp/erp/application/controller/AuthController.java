package com.erp.erp.application.controller;

import com.erp.erp.application.login.AuthService;
import com.erp.erp.domain.dto.ClientSignupRequest;
import com.erp.erp.domain.dto.LoginRequest;
import com.erp.erp.domain.dto.UserSignupRequest;
import com.erp.erp.domain.dto.response.APIResponse;
import com.erp.erp.infrastructure.component.JwtUtil;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    record AuthResponse(String token, List<String> roles) {}
    record ChangePasswordRequest(String oldPassword, String newPassword) {}


    public AuthController(AuthService authService, AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authService = authService;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/client/signup")
    public ResponseEntity<String> clientSignUp(@RequestBody ClientSignupRequest clientSignupRequest) {
        System.out.println(clientSignupRequest);
        APIResponse clientSignupResponse = authService.createNewClient(clientSignupRequest);
        return ResponseEntity.status(clientSignupResponse.getStatus()).body(clientSignupResponse.getResponse());
    }
    @PostMapping("/signup")
    public ResponseEntity<?> userSignUp(@RequestBody UserSignupRequest userSignupRequest) {
        System.out.println(userSignupRequest.getUserRoles() + userSignupRequest.getUserPhoneNumber());
        HttpStatus status = authService.createNewUser(userSignupRequest);
        if (status == HttpStatus.CONFLICT) {
            return ResponseEntity.status(status).body("Account already exists.");
        }
        return ResponseEntity.status(status).body("Account created.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.getUserEmail() + loginRequest.getPassword());
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserEmail(), loginRequest.getPassword())
            );
        }
        catch (AuthenticationException ex) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
        }
        UserDetails user = authService.loadUserByUsername(loginRequest.getUserEmail());
        String token = jwtUtil.generateToken(user);
        List<String> roles = user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        return ResponseEntity.ok(new AuthResponse(token, roles));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        String email = principal.getName();
        try {
            authService.changePassword(email, request.oldPassword(), request.newPassword());
        }
        catch (UsernameNotFoundException ex) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
        }
        catch (BadCredentialsException ex) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
        }
        return ResponseEntity.ok("Password changed for " + email);
    }
}
