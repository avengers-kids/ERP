package com.erp.erp.application.controller;

import com.erp.erp.application.dto.AccountInfoDto;
import com.erp.erp.application.dto.AddItemRequest;
import com.erp.erp.application.dto.BillDto;
import com.erp.erp.application.dto.CartItemDTO;
import com.erp.erp.application.dto.InvoiceDto;
import com.erp.erp.application.dto.StatusUpdateRequest;
import com.erp.erp.application.dto.TicketDto;
import com.erp.erp.application.dto.TicketStatusCount;
import com.erp.erp.application.item.CartService;
import com.erp.erp.application.login.AuthService;
import com.erp.erp.application.ticket.TicketService;
import com.erp.erp.domain.enums.TicketStatus;
import com.erp.erp.domain.model.item.Cart;
import com.erp.erp.domain.model.item.CartItem;
import com.erp.erp.domain.model.ticket.Ticket;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  private final AuthService authService;
  private final CartService cartService;

  public TicketController(TicketService ticketService,
      AuthService authService, CartService cartService) {
    this.ticketService = ticketService;
    this.authService = authService;
    this.cartService = cartService;
  }

//  @PostMapping("/create-ticket")
//  //ROLE-> ROLE_USER, ROLE_MANAGER
//  //@PreAuthorize("hasRole('MANAGER') or hasRole('USER') or hasRole('ADMIN')")
//  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
//  public ResponseEntity<?> newPurchaseTicket(@RequestBody TicketDto ticketDto, Principal principal) {
//    String email = principal.getName();
//    Long newTicketId = ticketService.createTicket(ticketDto, email);
//    return ResponseEntity.status(HttpStatus.CREATED).body("New Ticket has been created with ID : " + newTicketId);
//  }

  @PostMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<?> updateStatus(
      @PathVariable Long id,
      @Valid @RequestBody StatusUpdateRequest req
  ) {
    try {
      ticketService.updateTicketStatus(id, req.newStatus(), req.comment(), req.cost());
      return ResponseEntity.ok("TicketStatus updated to " + req.newStatus() + " for " + id);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PostMapping("/{id}/create-bill")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<?> createBill(@PathVariable Long id, @RequestBody BillDto billDto) {
    try {
      ticketService.createBillAndMoveToSold(id, billDto);
      return ResponseEntity.ok("Bill Created for Ticket " + id);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.internalServerError().body(ex.getMessage());
    }
  }

  @GetMapping("/search-ticket/{status}")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<?> searchTickets(@PathVariable TicketStatus status,
      @AuthenticationPrincipal(expression = "username") String email) {
    List<Ticket> tickets = ticketService.searchTickets(status, email);
    return ResponseEntity.ok(tickets);
  }

  @GetMapping("/search-ticket")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<List<Ticket>> searchTicket(
      @RequestParam Map<String, String> allParams,
      @AuthenticationPrincipal(expression = "username") String username
  ) {
    return ResponseEntity.ok(ticketService.findTicketBySpecification(allParams, username));
  }

  @GetMapping("check-ticket/{id}")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<?> checkTicket(@PathVariable Long id) {
    return ResponseEntity.ok(ticketService.checkTicket(id));
  }

  @GetMapping("/check-bill/{id}")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<?> checkBill(@PathVariable Long id) {
    return ResponseEntity.ok(ticketService.checkBill(id));
  }

  @GetMapping("/inventory-ticket")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<List<Ticket>> searchInventory(
      @RequestParam Map<String, String> allParams,
      @AuthenticationPrincipal(expression = "username") String username
  ) {
    return ResponseEntity.ok(ticketService.findInventoryTicketBySpecification(allParams, username));
  }

  @GetMapping("/my-account")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<AccountInfoDto> fetchAccountInfo(
      @AuthenticationPrincipal(expression = "username") String username) {
    return ResponseEntity.ok(authService.fetchUserInfo(username));
  }

  @PostMapping("/cart/items/add")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<String> addItem(
      @AuthenticationPrincipal(expression = "username") String username,
      @RequestBody AddItemRequest req) {
    Cart updated;
    if (Objects.equals(req.getCartType(), "BUY")) {
      updated = cartService.addBuyItem(username, req);
    } else if (Objects.equals(req.getCartType(), "SELL")) {
      return ResponseEntity.internalServerError().body("Cart Type can be BUY or SELL only.");
    } else {
      return ResponseEntity.internalServerError().body("Cart Type can be BUY or SELL only.");
    }
    return ResponseEntity.ok("Item added with Cart ID " + updated.getId());
  }

  /**
   * Delete a single cart item (and its details) by cartItemId
   */
  @DeleteMapping("/cart/items/clear/{cartItemDetailId}")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<Void> deleteItem(
      @AuthenticationPrincipal(expression = "username") String username,
      @PathVariable Long cartItemDetailId) {
    cartService.deleteItem(username, cartItemDetailId);
    return ResponseEntity.ok().build();
  }

  /**
   * Clear entire cart for the current user
   */
  @DeleteMapping("/cart/items/clear")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<String> clearCart(
      @AuthenticationPrincipal(expression = "username") String username, @RequestParam("cartType") String cartType) {
    Cart cart;
    if (Objects.equals(cartType, "BUY")) {
      cart = cartService.getBuyCart(username);
    } else if (Objects.equals(cartType, "SELL")) {
      cart = cartService.getSellCart(username);
    } else {
      return ResponseEntity.internalServerError().body("Cart Type can only be BUY or SELL");
    }
    cartService.clearCart(cart);
    return ResponseEntity.ok("Cart cleared with ID " + cart.getId());
  }

  /**
   * Checkout: create tickets for each cart detail and clear the cart
   */
  @PostMapping("/cart/items/buy/checkout")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<String> buyCheckout(
      @AuthenticationPrincipal(expression = "username") String username, @RequestBody InvoiceDto invoiceDto) {
    ticketService.checkoutForBuyCart(username, invoiceDto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/cart/items/sell/checkout")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<String> sellCheckout(
      @AuthenticationPrincipal(expression = "username") String username, @RequestBody InvoiceDto invoiceDto) {
    ticketService.checkoutForSellCart(username, invoiceDto);
    return ResponseEntity.ok().build();
  }

//  @GetMapping("/cart/items/a")
//  public ResponseEntity<PagedModel<EntityModel<CartItem>>> getCartItems(
//      @AuthenticationPrincipal(expression = "username") String username, @RequestParam("cartType") String cartType,
//      @PageableDefault(size = 1) Pageable pageable, PagedResourcesAssembler<CartItem> assembler) {
//    if (Objects.equals(cartType, "BUY")) {
//      return ResponseEntity.ok(assembler.toModel(cartService.getBuyCartAsPage(username, pageable)));
//    } else if (Objects.equals(cartType, "SELL")) {
//      return ResponseEntity.ok(assembler.toModel(cartService.getSellCartAsPage(username, pageable)));
//    } else {
//      return ResponseEntity.internalServerError().body(assembler.toModel(Page.empty(pageable)));
//    }
//  }

  @GetMapping("/cart/items")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<Page<CartItemDTO>> getCartItems(
      @AuthenticationPrincipal(expression = "username") String username, @RequestParam("cartType") String cartType,
      @PageableDefault(size = 1) Pageable pageable, PagedResourcesAssembler<CartItem> assembler) {
    if (Objects.equals(cartType, "BUY")) {
      return ResponseEntity.ok(cartService.getBuyCartAsPage(username, pageable));
    } else if (Objects.equals(cartType, "SELL")) {
      return ResponseEntity.ok(cartService.getSellCartAsPage(username, pageable));
    } else {
      return ResponseEntity.internalServerError().body(Page.empty(pageable));
    }
  }

  @GetMapping("/search-ticket/all-status/count")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<?> fetchAllStatusCount(
      @AuthenticationPrincipal(expression = "username") String username) {
    List<TicketStatusCount> ticketStatusCounts;
    try {
      ticketStatusCounts = ticketService.getTicketCountsByStatus();
    }
    catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
    Map<String,Long> countMap = ticketStatusCounts.stream()
        .collect(Collectors.toMap(
            c -> c.getStatus().name(),
            TicketStatusCount::getCount
        ));
    if (ticketStatusCounts == null) {
      return ResponseEntity.noContent().build();
    }
    else {
      return ResponseEntity.ok(countMap);
    }
  }


}
