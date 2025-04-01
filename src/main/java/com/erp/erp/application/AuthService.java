package com.erp.erp.application;

import com.erp.erp.domain.model.user.User;
import com.erp.erp.domain.model.user.UserRepository;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String username, String rawPassword) {
        System.out.println(username + rawPassword);
        Optional<User> userOpt = userRepository.findByUserName(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("user password:, raw password:, username:" + user.getPassword() + " " + rawPassword + " " + username);
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }
}
