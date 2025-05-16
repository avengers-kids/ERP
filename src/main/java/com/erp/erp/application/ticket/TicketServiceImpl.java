package com.erp.erp.application.ticket;

import com.erp.erp.domain.dto.TicketDto;
import com.erp.erp.domain.enums.TicketStatus;
import com.erp.erp.domain.model.ticket.Ticket;
import com.erp.erp.domain.model.ticket.TicketRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class TicketServiceImpl implements TicketService{
  private final TicketRepository ticketRepository;

  public Long createTicket(TicketDto ticketDto) {
    return createATicketForNewPurchase(ticketDto);
  }

  private Long createATicketForNewPurchase(TicketDto ticketDto) {
    Ticket newTicket = Ticket.builder()
        .clientId(ticketDto.clientId())
        .ticketStatus(TicketStatus.PURCHASED)
        .invoiceNumber(ticketDto.invoiceNumber())
        .invoiceDate(ticketDto.invoiceDate())
        .phoneNumber(ticketDto.phoneNumber())
        .customerName(ticketDto.customerName())
        .gstNumber(ticketDto.gstNumber())
        .gstId(ticketDto.gstId())
        .productPurchaseType(ticketDto.productPurchaseType())
        .modeOfPayment(ticketDto.modeOfPayment())
        .customerAadharId(ticketDto.customerAadharId())
        .itemId(ticketDto.itemId())
        .isDeleted("N")
        .build();
    ticketRepository.save(newTicket);
    return newTicket.getTicketId();
  }

}
