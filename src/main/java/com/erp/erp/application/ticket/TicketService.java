package com.erp.erp.application.ticket;

import com.erp.erp.domain.dto.TicketDto;

public interface TicketService {

  Long createTicket(TicketDto ticketDto);

}
