package com.erp.erp.application.controller;

import com.erp.erp.application.dto.BillDto;
import com.erp.erp.application.dto.StatusUpdateRequest;
import com.erp.erp.application.dto.TicketDto;
import com.erp.erp.application.ticket.TicketService;
import com.erp.erp.domain.enums.TicketStatus;
import com.erp.erp.domain.model.ticket.Ticket;
import com.erp.erp.domain.model.ticket.TicketRepository;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ticket")
@PreAuthorize("isAuthenticated()")
public class TicketController {

  private final TicketService ticketService;
  private final TicketRepository ticketRepository;

  public TicketController(TicketService ticketService,
      TicketRepository ticketRepository) {
    this.ticketService = ticketService;
    this.ticketRepository = ticketRepository;
  }

  @PostMapping("/create-ticket")
  //ROLE-> ROLE_USER, ROLE_MANAGER
  //@PreAuthorize("hasRole('MANAGER') or hasRole('USER') or hasRole('ADMIN')")
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
      ticketService.updateTicketStatus(id, req.newStatus(), req.comment(), req.cost());
      return ResponseEntity.ok("TicketStatus updated to " + req.newStatus() + " for " + id);
    }
    catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PostMapping("/{id}/create-bill")
  public ResponseEntity<?> createBill(@PathVariable Long id, @RequestBody BillDto billDto) {
    try {
      ticketService.createBillAndMoveToSold(id, billDto);
      return ResponseEntity.ok("Bill Created for Ticket " + id);
    }
    catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
    catch (Exception ex) {
      return ResponseEntity.internalServerError().body(ex.getMessage());
    }
  }

  @GetMapping("/search-ticket/{status}")
  public ResponseEntity<?> searchTickets(@PathVariable TicketStatus status, @AuthenticationPrincipal(expression = "username") String email) {
    List<Ticket> tickets = ticketService.searchTickets(status, email);
    return ResponseEntity.ok(tickets);
  }

  @GetMapping("/search-ticket")
  public ResponseEntity<List<Ticket>> searchTicket(
      @RequestParam Map<String,String> allParams,
      @AuthenticationPrincipal(expression="username") String username
  ) {
    return ResponseEntity.ok(ticketService.findTicketBySpecification(allParams, username));
  }

  @GetMapping("check-ticket/{id}")
  public ResponseEntity<?> checkTicket(@PathVariable Long id) {
    return ResponseEntity.ok(ticketService.checkTicket(id));
  }

  @GetMapping("/check-bill/{id}")
  public ResponseEntity<?> checkBill(@PathVariable Long id) {
    return ResponseEntity.ok(ticketService.checkBill(id));
  }

}
