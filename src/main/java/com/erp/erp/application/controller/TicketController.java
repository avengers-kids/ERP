package com.erp.erp.application.controller;

import com.erp.erp.application.dto.StatusUpdateRequest;
import com.erp.erp.application.ticket.TicketService;
import com.erp.erp.application.dto.TicketDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @PostMapping("/{id}/status")
  public ResponseEntity<?> updateStatus(
      @PathVariable Long id,
      @Valid @RequestBody StatusUpdateRequest req
  ) {
    try {
      ticketService.updateTicketStatus(id, req.newStatus());
      return ResponseEntity.ok("TicketStatus updated to " + req.newStatus() + " for " + id);
    }
    catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}
