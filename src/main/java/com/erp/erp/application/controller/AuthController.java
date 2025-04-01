package com.erp.erp.application.controller;

import com.erp.erp.application.login.AuthService;
import com.erp.erp.domain.dto.response.APIResponse;
import com.erp.erp.domain.dto.ClientSignupRequest;
import com.erp.erp.domain.dto.LoginRequest;
import com.erp.erp.domain.dto.response.NewUserResponse;
import com.erp.erp.domain.dto.UserSignupRequest;
import com.erp.erp.domain.dto.response.LogInResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/client/signup")
    public ResponseEntity<String> clientSignUp(@RequestBody ClientSignupRequest clientSignupRequest) {
        System.out.println(clientSignupRequest);
        APIResponse clientSignupResponse = authService.createNewClient(clientSignupRequest);
        return ResponseEntity.status(clientSignupResponse.getStatus()).body(clientSignupResponse.getResponse());
    }
    @PostMapping("/user/signup")
    public ResponseEntity<NewUserResponse> userSignUp(@RequestBody UserSignupRequest userSignupRequest) {
        System.out.println(userSignupRequest.getUserRoles() + userSignupRequest.getUserPhoneNumber());
        NewUserResponse signUpResponse = authService.createNewUser(userSignupRequest);
        return ResponseEntity.status(signUpResponse.getStatus()).body(signUpResponse);
    }

    @GetMapping("/login")
    public ResponseEntity<LogInResponse> logIn(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.getUserEmail() + loginRequest.getPassword());
        LogInResponse loginResponse = authService.authenticate(loginRequest.getUserEmail(), loginRequest.getPassword());

//        Authentication authentication = authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Generate JWT
//        String token = jwtUtil.generateToken(request.getUsername());
//
//        // Store session attributes
//        session.setAttribute("username", request.getUsername());
        return ResponseEntity.status(loginResponse.getStatus()).body(loginResponse);
    }
}
