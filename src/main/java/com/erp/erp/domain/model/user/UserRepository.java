package com.erp.erp.domain.model.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmailAndPassword(String userEmail, String password);
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByUserEmailAndClientId(String userEmail, Long clientId);
}
