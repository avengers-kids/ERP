package com.erp.erp.application.ticket;

import com.erp.erp.application.dto.BillDto;
import com.erp.erp.application.dto.TicketDto;
import com.erp.erp.domain.enums.TicketStatus;
import com.erp.erp.domain.model.ticket.Ticket;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TicketService {

  Long createTicket(TicketDto ticketDto, String email);

  void updateTicketStatus(Long ticketId, TicketStatus newTicketStatus, String comment, BigDecimal cost);

  void createBillAndMoveToSold(Long ticketId, BillDto billDto);

  List<Ticket> searchQC1Data(String email);

  List<Ticket> searchTickets(TicketStatus status, String email);

  List<Ticket> searchTicketsByUserName(String email);

  List<Ticket> findTicketBySpecification(Map<String, String> allParams, String username);

  BillDto checkBill(Long ticketId);

  Ticket checkTicket(Long ticketId);
}
