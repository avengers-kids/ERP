package com.erp.erp.application.ticket;

import com.erp.erp.application.dto.TicketDto;
import com.erp.erp.domain.enums.TicketStatus;

public interface TicketService {

  Long createTicket(TicketDto ticketDto);

  void updateTicketStatus(Long ticketId, TicketStatus newTicketStatus);
}
