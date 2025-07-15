package com.erp.erp.domain.model.item;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserEmailAndCartType(String userEmail, String cartType);

    Optional<Cart> findByUserEmail(String userEmail);

}
