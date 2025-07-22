package com.erp.erp.domain.model.item;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserEmailAndCartType(String userEmail, String cartType);

    Optional<Cart> findByUserEmail(String userEmail);

    @Query("""
  SELECT c FROM Cart c
  LEFT JOIN FETCH c.items i
  WHERE c.userEmail = :email AND c.cartType = :type
""")
    Optional<Cart> findCartWithItems(@Param("email") String email, @Param("type") String cartType);



}
