package com.erp.erp.application.controller;

import com.erp.erp.application.dto.StatusUpdateRequest;
import com.erp.erp.application.dto.TicketDto;
import com.erp.erp.application.ticket.TicketService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
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
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<?> newPurchaseTicket(@RequestBody TicketDto ticketDto, Principal principal) {
    String email = principal.getName();
    Long newTicketId = ticketService.createTicket(ticketDto, email);
    return ResponseEntity.status(HttpStatus.CREATED).body("New Ticket has been created with ID : " + newTicketId);
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

  @PostMapping("/{id}/create-bill")
  public ResponseEntity<?> createBill(@PathVariable Long id) {
    try {
      ticketService.createBillAndMoveToSold(id);
      return ResponseEntity.ok("Bill Created for Ticket " + id);
    }
    catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
    catch (Exception ex) {
      return ResponseEntity.internalServerError().body(ex.getMessage());
    }
  }
}
