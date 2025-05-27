package com.erp.erp.application.controller;

import com.erp.erp.application.dto.ClientSignupRequest;
import com.erp.erp.application.dto.LoginRequest;
import com.erp.erp.application.dto.UserSignupRequest;
import com.erp.erp.application.dto.response.APIResponse;
import com.erp.erp.application.login.AuthService;
import com.erp.erp.application.login.UserTokenService;
import com.erp.erp.infrastructure.component.JwtUtil;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final AuthenticationManager authManager;
  private final JwtUtil jwtUtil;
  private final UserTokenService userTokenService;

  record AuthResponse(String token, List<String> roles) {

  }

  record ChangePasswordRequest(String oldPassword, String newPassword) {

  }


  public AuthController(AuthService authService, AuthenticationManager authManager, JwtUtil jwtUtil,
      UserTokenService userTokenService) {
    this.authService = authService;
    this.authManager = authManager;
    this.jwtUtil = jwtUtil;
    this.userTokenService = userTokenService;
  }

  @PostMapping("/client/signup")
  public ResponseEntity<String> clientSignUp(@RequestBody ClientSignupRequest clientSignupRequest) {
    APIResponse clientSignupResponse = authService.createNewClient(clientSignupRequest);
    return ResponseEntity.status(clientSignupResponse.getStatus()).body(clientSignupResponse.getResponse());
  }

  @PostMapping("/signup")
  public ResponseEntity<?> userSignUp(@RequestBody UserSignupRequest userSignupRequest) {
    System.out.println(userSignupRequest.getUserRoles() + userSignupRequest.getUserPhoneNumber());
    HttpStatus status = authService.createNewUser(userSignupRequest);
    if (status != HttpStatus.CREATED) {
      return ResponseEntity.status(status).body("Email is invalid or account already exists!");
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
    } catch (AuthenticationException ex) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("Invalid username or password");
    }
    UserDetails user = authService.loadUserByUsername(loginRequest.getUserEmail());
    String token = jwtUtil.generateToken(user);

    String jti = jwtUtil.extractJti(token);
    Date issuedAt = jwtUtil.extractIssuedAt(token);
    Date expiresAt = jwtUtil.extractExpiration(token);
    userTokenService.saveToken(loginRequest.getUserEmail(), jti, issuedAt, expiresAt);
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
    } catch (UsernameNotFoundException ex) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(ex.getMessage());
    } catch (BadCredentialsException ex) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(ex.getMessage());
    }
    return ResponseEntity.ok("Password changed for " + email);
  }

  @GetMapping("/refresh-token")
  public ResponseEntity<Map<String, String>> refreshToken(
      @RequestHeader("Authorization") String authHeader) {

    // 1) Extract the raw token
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(Map.of("error", "Missing or invalid Authorization header"));
    }
    String oldToken = authHeader.substring(7);

    // 2) Delegate to the service
    String newToken = authService.refreshToken(oldToken);

    // 3) Return the new token
    return ResponseEntity.ok(Map.of("accessToken", newToken));
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
    }
    String token = authHeader.substring(7);
    String jti = jwtUtil.extractJti(token);
    String username = jwtUtil.extractUsername(token);

    userTokenService.revokeToken(username, jti);
    SecurityContextHolder.clearContext();
    return ResponseEntity.ok("Logged out successfully");
  }
}
