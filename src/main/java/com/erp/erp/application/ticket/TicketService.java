package com.erp.erp.application.ticket;

import com.erp.erp.application.dto.TicketDto;
import com.erp.erp.domain.enums.TicketStatus;
import com.erp.erp.domain.model.ticket.Ticket;
import java.util.List;

public interface TicketService {

  Long createTicket(TicketDto ticketDto, String email);

  void updateTicketStatus(Long ticketId, TicketStatus newTicketStatus);

  void createBillAndMoveToSold(Long ticketId);

  List<Ticket> searchQC1Data(String email);
}
