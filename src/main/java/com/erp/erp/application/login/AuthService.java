package com.erp.erp.application.login;

import com.erp.erp.domain.dto.ClientSignupRequest;
import com.erp.erp.domain.dto.UserSignupRequest;
import com.erp.erp.domain.dto.response.APIResponse;
import com.erp.erp.domain.dto.response.LogInResponse;
import com.erp.erp.domain.dto.response.NewUserResponse;
import com.erp.erp.domain.model.client.Client;
import com.erp.erp.domain.model.client.ClientRepository;
import com.erp.erp.domain.model.user.User;
import com.erp.erp.domain.model.user.UserRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService implements UserDetailsService {
//    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final long ONE = 1;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public LogInResponse authenticate(String userEmail, String rawPassword) {
        System.out.println(userEmail + rawPassword);
        Optional<User> userOpt = userRepository.findByUserEmail(userEmail);
        if (userOpt.isPresent()) {
            if (passwordEncoder.matches(rawPassword, userOpt.get().getPassword())) {
                String createdResponse = "Logged in successfully";
                return LogInResponse.builder()
                    .status(HttpStatus.OK)
                    .userEmail(userEmail)
                    .message(createdResponse)
                    .build();
            }
            else {
                String authFailResponse = "Password is case sensitive";
                return LogInResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .userEmail(userEmail)
                    .message(authFailResponse)
                    .build();
            }

        }
        else {
            String authFailResponse = "No account exists with your email!";
            return LogInResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .userEmail(userEmail)
                .message(authFailResponse)
                .build();
        }
    }

    public HttpStatus createNewUser(UserSignupRequest userSignupRequest) {
        System.out.println(userSignupRequest.getUserEmail() + userSignupRequest.getUserRoles());
        Optional<User> userOpt = userRepository.findByUserEmail(userSignupRequest.getUserEmail());
        if (userOpt.isPresent()) {
            return HttpStatus.CONFLICT;
        }
        else {
            createUser(userSignupRequest);
            return HttpStatus.CREATED;
        }
    }

    private User createUser(UserSignupRequest userSignupRequest) {
        String encodedPassword = passwordEncoder.encode(userSignupRequest.getPassword());
        User newUser = User.builder()
            .userId(userSignupRequest.getUserId())
            .userName(userSignupRequest.getUserEmail())
            .userEmail(userSignupRequest.getUserEmail())
            .password(encodedPassword)
            .clientId(userSignupRequest.getClientId())
            .userPhoneNumber(userSignupRequest.getUserPhoneNumber())
            .userRoles(userSignupRequest.getUserRoles())
            .createdBy(userSignupRequest.getUserEmail())
            .lastUpdatedBy(userSignupRequest.getUserEmail())
            .creationDate(Instant.now())
            .lastUpdateDate(Instant.now())
            .version(ONE)
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
        }
        else {
            createClient(clientSignupRequest);
            String createdResponse = "New account created with email " + clientSignupRequest.getClientEmail();
            return APIResponse.builder().status(HttpStatus.OK).response(createdResponse).build();
        }
    }

    private void createClient(ClientSignupRequest clientSignupRequest) {
        Client newClient = Client.builder()
            .clientId(clientSignupRequest.getClientId())
            .clientName(clientSignupRequest.getClientName())
            .email(clientSignupRequest.getClientEmail())
            .phoneNumber(clientSignupRequest.getClientPhoneNumber())
            .build();
        clientRepository.save(newClient);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUserEmail(username);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User
            .withUsername(userOpt.get().getUserEmail())
            .password(userOpt.get().getPassword())
            .authorities(userOpt.get().getUserRoles().split(","))
            .build();
    }
}
