package com.erp.erp.application.login;

import com.erp.erp.application.dto.ClientSignupRequest;
import com.erp.erp.application.dto.UserSignupRequest;
import com.erp.erp.application.dto.response.APIResponse;
import com.erp.erp.application.dto.response.LogInResponse;
import com.erp.erp.domain.model.client.Client;
import com.erp.erp.domain.model.client.ClientRepository;
import com.erp.erp.domain.model.user.User;
import com.erp.erp.domain.model.user.UserRepository;
import com.erp.erp.infrastructure.component.JwtUtil;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService implements UserDetailsService {

  private static final long ONE = 1;
  private final UserRepository userRepository;
  private final ClientRepository clientRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final UserTokenService userTokenService;

  public HttpStatus createNewUser(UserSignupRequest userSignupRequest) {
    Optional<User> userOpt = userRepository.findByUserEmail(userSignupRequest.getUserEmail());
    if (userOpt.isPresent()) {
      return HttpStatus.CONFLICT;
    } else if (clientRepository.findById(userSignupRequest.getClientId()).isEmpty()) {
      return HttpStatus.NOT_FOUND;
    } else {
      createUser(userSignupRequest);
      return HttpStatus.CREATED;
    }
  }

  private User createUser(UserSignupRequest userSignupRequest) {
    String encodedPassword = passwordEncoder.encode(userSignupRequest.getPassword());
    User newUser = User.builder()
        .userName(userSignupRequest.getUserEmail())
        .userEmail(userSignupRequest.getUserEmail())
        .password(encodedPassword)
        .clientId(userSignupRequest.getClientId())
        .userPhoneNumber(userSignupRequest.getUserPhoneNumber())
        .userRoles(userSignupRequest.getUserRoles())
        .build();
    userRepository.save(newUser);
    return newUser;
  }

  public APIResponse createNewClient(ClientSignupRequest clientSignupRequest) {
    System.out.println(clientSignupRequest.getClientName() + clientSignupRequest.getClientEmail());
    Optional<Client> clientOpt = clientRepository.findByEmail(clientSignupRequest.getClientEmail());
    if (clientOpt.isPresent()) {
      String conflictResponse = "An account already exists with email "
          + clientSignupRequest.getClientEmail();
      return APIResponse.builder().status(HttpStatus.CONFLICT).response(conflictResponse).build();
    } else {
      Long clientId = createClient(clientSignupRequest);
      String createdResponse =
          "New account created with email " + clientSignupRequest.getClientEmail() + "and Id " + clientId;
      return APIResponse.builder().status(HttpStatus.OK).response(createdResponse).build();
    }
  }

  private Long createClient(ClientSignupRequest clientSignupRequest) {
    Client newClient = Client.builder()
        .clientName(clientSignupRequest.getClientName())
        .email(clientSignupRequest.getClientEmail())
        .phoneNumber(clientSignupRequest.getClientPhoneNumber())
        .build();
    clientRepository.save(newClient);
    return newClient.getClientId();
  }

  public void changePassword(String email, String oldPwd, String newPwd) {
    Optional<User> userOpt = userRepository.findByUserEmail(email);
    if (userOpt.isEmpty()) {
      throw new UsernameNotFoundException("User not found");
    }

    if (!passwordEncoder.matches(oldPwd, userOpt.get().getPassword())) {
      throw new BadCredentialsException("Old password is incorrect");
    }
    userOpt.get().changePassword(email, passwordEncoder.encode(newPwd));
    userRepository.save(userOpt.get());
  }

  /**
   * Authenticate credentials, issue a JWT, persist its JTI, and return it.
   */
  public LogInResponse authenticate(String userEmail, String rawPassword) {
    User user = userRepository.findByUserEmail(userEmail)
        .orElseThrow(() -> new BadCredentialsException("No account exists with your email!"));

    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
      throw new BadCredentialsException("Invalid password");
    }

    // 1) Load Spring Security UserDetails
    UserDetails userDetails = loadUserByUsername(userEmail);

    // 2) Generate token
    String token = jwtUtil.generateToken(userDetails);

    // 3) Persist the JTI & timestamps (revoking any prior token for this user)
    String jti       = jwtUtil.extractJti(token);
    Date issuedAt    = jwtUtil.extractIssuedAt(token);
    Date expiresAt   = jwtUtil.extractExpiration(token);
    userTokenService.saveToken(userEmail, jti, issuedAt, expiresAt);

    // 4) Return the token in your response DTO
    return LogInResponse.builder()
        .status(HttpStatus.OK)
        .userEmail(userEmail)
        .accessToken(token)
        .message("Logged in successfully")
        .build();
  }

  /**
   * Refresh an existing but unexpired token:
   *  - revoke the old JTI
   *  - issue & persist a fresh token
   */
  public String refreshToken(String oldToken) {
    String username = jwtUtil.extractUsername(oldToken);
    String oldJti   = jwtUtil.extractJti(oldToken);

    // Revoke the old token
    userTokenService.revokeToken(username, oldJti);

    // Issue a fresh JWT
    UserDetails userDetails = loadUserByUsername(username);
    String newToken = jwtUtil.generateToken(userDetails);

    // Persist its JTI & timestamps
    String newJti     = jwtUtil.extractJti(newToken);
    Date newIssuedAt  = jwtUtil.extractIssuedAt(newToken);
    Date newExpiresAt = jwtUtil.extractExpiration(newToken);
    userTokenService.saveToken(username, newJti, newIssuedAt, newExpiresAt);

    return newToken;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUserEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return org.springframework.security.core.userdetails.User
        .withUsername(user.getUserEmail())
        .password(user.getPassword())
        .authorities(user.getUserRoles().split(","))
        .build();
  }
}
