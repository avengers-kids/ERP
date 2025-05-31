package com.erp.erp.domain.model.ticket;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoldStatusRepository extends JpaRepository<SoldStatus, Long> {
    Optional<SoldStatus> findBySoldTableId(Long soldTableId);

    Optional<SoldStatus> findByTicketId(Long ticketId);

}
