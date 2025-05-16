package com.erp.erp.domain.model.ticket;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketLifeCycleRepository extends JpaRepository<TicketLifecycle, Long> {
  Optional<TicketLifecycle> findByTicketLcId(Long ticketLcId);
}
