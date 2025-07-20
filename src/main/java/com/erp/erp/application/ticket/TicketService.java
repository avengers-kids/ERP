package com.erp.erp.application.ticket;

import com.erp.erp.application.dto.BillDto;
import com.erp.erp.application.dto.InvoiceDto;
import com.erp.erp.application.dto.TicketDto;
import com.erp.erp.application.dto.TicketStatusCount;
import com.erp.erp.application.dto.response.TicketResponseDto;
import com.erp.erp.domain.enums.TicketStatus;
import com.erp.erp.domain.model.ticket.SoldStatus;
import com.erp.erp.domain.model.ticket.Ticket;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {

  Long createTicket(TicketDto ticketDto, String email);

  void updateTicketStatus(Long ticketId, TicketStatus newTicketStatus, String comment, BigDecimal cost);

//  void createBillAndMoveToSold(Long ticketId, BillDto billDto);

  List<Ticket> searchQC1Data(String email);

  Page<TicketResponseDto> searchTickets(TicketStatus status, String email, Pageable pageable);

  List<Ticket> searchTicketsByUserName(String email);

  Page<TicketResponseDto> findTicketBySpecification(Map<String, String> allParams, String username, Pageable pageable);

  List<Ticket> findInventoryTicketBySpecification(Map<String, String> allParams, String username);

  BillDto checkBill(Long ticketId);

  Ticket checkTicket(Long ticketId);

  List<Ticket> checkoutForBuyCart(String userEmail, InvoiceDto invoiceDto);

  List<SoldStatus> checkoutSellCart(String userEmail, BillDto billDto);

  List<TicketStatusCount> getTicketCountsByStatus(String username);

}
