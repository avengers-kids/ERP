package com.erp.erp.domain.model.ticket;

import com.erp.erp.domain.enums.TicketStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByTicketId(Long ticketId);

    List<Ticket> findByTicketStatusAndUserEmail(TicketStatus status, String userEmail);

}
