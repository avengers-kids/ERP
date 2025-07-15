package com.erp.erp.domain.model.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  Page<CartItem> findByCartId(Long cartId, Pageable pageable);
}
