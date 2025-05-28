package com.erp.erp.application.ticket;

import com.erp.erp.application.dto.TicketDto;
import com.erp.erp.domain.enums.TicketStatus;
import com.erp.erp.domain.model.item.PhoneDetails;
import com.erp.erp.domain.model.item.PhoneDetailsRepository;
import com.erp.erp.domain.model.ticket.SoldStatus;
import com.erp.erp.domain.model.ticket.SoldStatusRepository;
import com.erp.erp.domain.model.ticket.Ticket;
import com.erp.erp.domain.model.ticket.TicketLifeCycleRepository;
import com.erp.erp.domain.model.ticket.TicketLifecycle;
import com.erp.erp.domain.model.ticket.TicketRepository;
import com.erp.erp.domain.model.user.User;
import com.erp.erp.domain.model.user.UserRepository;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class TicketServiceImpl implements TicketService{
  private final TicketRepository ticketRepository;
  private final TicketLifeCycleRepository ticketLifeCycleRepository;
  private static final Map<TicketStatus, Map<TicketStatus, Set<String>>> TRANSITION_ROLE_MAP;
  private static final String MANAGER = "ROLE_MANAGER";
  private static final String PURCHASED = "ROLE_PURCHASED_USER";
  private static final String QC1_USER = "ROLE_QC1_USER";
  private static final String QC2_USER = "ROLE_QC2_USER";
  private static final String QC3_USER = "ROLE_QC3_USER";
  private static final String QC4_USER = "ROLE_QC4_USER";
  private static final String LISTED_USER = "ROLE_LISTED_USER";
  private final SoldStatusRepository soldStatusRepository;
  private final PhoneDetailsRepository phoneDetailsRepository;
  private final UserRepository userRepository;

  static {
    TRANSITION_ROLE_MAP = new EnumMap<>(TicketStatus.class);
    TRANSITION_ROLE_MAP.put(TicketStatus.PURCHASED, Map.of(
        TicketStatus.QC1, Set.of(PURCHASED, MANAGER))
    );
    TRANSITION_ROLE_MAP.put(TicketStatus.QC1, Map.of(
        TicketStatus.QC2, Set.of(QC1_USER, MANAGER),
        TicketStatus.LISTED, Set.of(QC1_USER, MANAGER),
        TicketStatus.SCRAPED, Set.of(QC1_USER, MANAGER))
    );
    TRANSITION_ROLE_MAP.put(TicketStatus.QC2, Map.of(
        TicketStatus.QC3, Set.of(QC2_USER, MANAGER),
        TicketStatus.LISTED, Set.of(QC2_USER, MANAGER),
        TicketStatus.SCRAPED, Set.of(QC2_USER, MANAGER))
    );
    TRANSITION_ROLE_MAP.put(TicketStatus.QC3, Map.of(
        TicketStatus.QC4, Set.of(QC3_USER, MANAGER),
        TicketStatus.LISTED, Set.of(QC3_USER, MANAGER),
        TicketStatus.SCRAPED, Set.of(QC3_USER, MANAGER))
    );
    TRANSITION_ROLE_MAP.put(TicketStatus.QC4, Map.of(
        TicketStatus.LISTED, Set.of(QC4_USER, MANAGER),
        TicketStatus.SCRAPED, Set.of(QC4_USER, MANAGER))
    );
    TRANSITION_ROLE_MAP.put(TicketStatus.LISTED, Map.of(
        TicketStatus.SCRAPED, Set.of(LISTED_USER, MANAGER))
    );

  }

  public Long createTicket(TicketDto ticketDto, String email) {
    return createATicketForNewPurchase(ticketDto, email);
  }

  @Override
  public void updateTicketStatus(Long ticketId, TicketStatus newTicketStatus) {
    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));
    TicketStatus current = ticket.getTicketStatus();

    Map<TicketStatus, Set<String>> allowedMap = TRANSITION_ROLE_MAP.getOrDefault(current, Map.of());
    if (!allowedMap.containsKey(newTicketStatus)) {
      throw new IllegalArgumentException(
          "Invalid status transition from " + current + " to " + newTicketStatus);
    }

    Set<String> requiredRoles = allowedMap.get(newTicketStatus);
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean hasRole = auth.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(requiredRoles::contains);
    if (!hasRole) {
      throw new AccessDeniedException(
          "You need one of roles " + requiredRoles + " to make this transition");
    }
    TicketStatus oldTicketStatus = ticket.getTicketStatus();
    ticketRepository.save(ticket);
    ticket.setTicketStatus(newTicketStatus);
    TicketLifecycle ticketLifecycle = TicketLifecycle.builder()
        .ticketId(ticketId)
        .statusChangeTime(Instant.now())
        .prevTicketStatus(oldTicketStatus)
        .newTicketStatus(newTicketStatus)
        .build();
    ticketLifeCycleRepository.save(ticketLifecycle);
  }

  public void createBillAndMoveToSold(Long ticketId) {
    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(() -> new IllegalArgumentException("Ticket not found : " + ticketId));
    if (ticket.getTicketStatus() == TicketStatus.SOLD) {
      throw new IllegalArgumentException("Product already sold : " + ticketId);
    }
    else if (ticket.getTicketStatus() != TicketStatus.LISTED) {
      throw new IllegalArgumentException("Product need to be on Listed Status to be sold : " + ticketId);
    }
    else if (ticket.getIsDeleted().equalsIgnoreCase("Y")) {
      throw new IllegalArgumentException("This ticket is deleted : " + ticketId);
    }
    else {
      SoldStatus soldStatus = SoldStatus.builder()
          .ticketId(ticketId)
          .clientId(ticket.getClientId())
          .invoiceNumber(ticket.getInvoiceNumber())
          .invoiceDate(ticket.getInvoiceDate())
          .phoneNumber(ticket.getPhoneNumber())
          .customerName(ticket.getCustomerName())
          .gstNumber(ticket.getGstNumber())
          .gstId(ticket.getGstId())
          .purchaseType(ticket.getProductPurchaseType())
          .modeOfPayment(ticket.getModeOfPayment())
          .customerAadharId(ticket.getCustomerAadharId())
          .itemId(ticket.getItemId())
          .isDeleted("N")
          .build();
      soldStatusRepository.save(soldStatus);
    }
  }

  private Long createATicketForNewPurchase(TicketDto ticketDto, String email) {
    Optional<User> user = userRepository.findByUserEmail(email);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("No user found for user " + email);
    }
    Ticket newTicket = Ticket.builder()
        .clientId(user.get().getClientId())
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
        .acquisitionCost(ticketDto.acquisitionCost())
        .refurbishedCost(ticketDto.refurbishedCost())
        .isDeleted("N")
        .build();
    ticketRepository.save(newTicket);

    TicketLifecycle ticketLifecycle = TicketLifecycle.builder()
        .ticketId(newTicket.getTicketId())
        .statusChangeTime(Instant.now())
        .prevTicketStatus(null)
        .newTicketStatus(TicketStatus.PURCHASED)
        .build();
    ticketLifeCycleRepository.save(ticketLifecycle);
    return newTicket.getTicketId();
  }

}
