package com.erp.erp.domain.model.ticket;

import com.erp.erp.domain.enums.TicketStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

  Optional<Ticket> findByTicketId(Long ticketId);

  List<Ticket> findByTicketStatusAndUserEmail(TicketStatus status, String userEmail);

  List<Ticket> findByUserEmail(String userEmail);

}
