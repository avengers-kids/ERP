package com.erp.erp.application.controller;

import com.erp.erp.application.ticket.TicketService;
import com.erp.erp.domain.dto.TicketDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ticket")
@PreAuthorize("isAuthenticated()")
public class TicketController {

  private final TicketService ticketService;

  public TicketController(TicketService ticketService) {
    this.ticketService = ticketService;
  }

  @PostMapping("/create-ticket")
  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  public ResponseEntity<?> newPurchaseTicket(@RequestBody TicketDto ticketDto) {
    ticketService.createTicket(ticketDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("New Ticket has been created.");
  }
}
