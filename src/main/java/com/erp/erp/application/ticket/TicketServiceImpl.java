package com.erp.erp.application.ticket;

import com.erp.erp.application.dto.BillDto;
import com.erp.erp.application.dto.TicketDto;
import com.erp.erp.domain.enums.TicketStatus;
import com.erp.erp.domain.model.ticket.SoldStatus;
import com.erp.erp.domain.model.ticket.SoldStatusRepository;
import com.erp.erp.domain.model.ticket.Ticket;
import com.erp.erp.domain.model.ticket.TicketLifeCycleRepository;
import com.erp.erp.domain.model.ticket.TicketLifecycle;
import com.erp.erp.domain.model.ticket.TicketRepository;
import com.erp.erp.domain.model.user.User;
import com.erp.erp.domain.model.user.UserRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.EntityType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
public class TicketServiceImpl implements TicketService {

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
  public void updateTicketStatus(Long ticketId, TicketStatus newTicketStatus, String comment, BigDecimal cost) {
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
    String newComment = ticket.getComment() + comment;
    BigDecimal newRefurbishedCost = ticket.getRefurbishedCost().add(cost);
    ticket.setComment(newComment);
    ticket.setRefurbishedCost(newRefurbishedCost);
    ticketRepository.save(ticket);
    ticket.setTicketStatus(newTicketStatus);
    TicketLifecycle ticketLifecycle = TicketLifecycle.builder()
        .ticketId(ticketId)
        .userEmail(ticket.getUserEmail())
        .comment(comment)
        .statusChangeTime(Instant.now())
        .prevTicketStatus(oldTicketStatus)
        .newTicketStatus(newTicketStatus)
        .costAggregation(cost)
        .build();
    ticketLifeCycleRepository.save(ticketLifecycle);
  }

  public void createBillAndMoveToSold(Long ticketId, BillDto billDto) {
    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(() -> new IllegalArgumentException("Ticket not found : " + ticketId));
    if (ticket.getTicketStatus() == TicketStatus.SOLD) {
      throw new IllegalArgumentException("Product already sold : " + ticketId);
    } else if (ticket.getTicketStatus() != TicketStatus.LISTED) {
      throw new IllegalArgumentException("Product need to be on Listed Status to be sold : " + ticketId);
    } else if (ticket.getIsDeleted().equalsIgnoreCase("Y")) {
      throw new IllegalArgumentException("This ticket is deleted : " + ticketId);
    } else {
      ticket.setTicketStatus(TicketStatus.SOLD);
      SoldStatus soldStatus = SoldStatus.builder()
          .ticketId(ticketId)
          .clientId(ticket.getClientId())
          .phoneNumber(billDto.phoneNumber())
          .customerName(billDto.customerName())
          .phoneNumber(billDto.phoneNumber())
          .gstId(billDto.gstId())
          .onlineTrxId(billDto.onlineTrxId())
          .modeOfPayment(billDto.modeOfPayment())
          .placeOfSale(billDto.placeOfSale())
          .profit(billDto.profit())
          .billNumber(UUID.randomUUID().toString())
          .billDate(LocalDate.now())
          .isDeleted("N")
          .build();
      soldStatusRepository.save(soldStatus);
    }
  }

  @Override
  public List<Ticket> searchQC1Data(String email) {
    return ticketRepository.findByTicketStatusAndUserEmail(TicketStatus.QC1, email);
  }

  @Override
  public List<Ticket> searchTickets(TicketStatus status, String email) {
    return ticketRepository.findByTicketStatusAndUserEmail(status, email);
  }

  @Override
  public List<Ticket> searchTicketsByUserName(String email) {
    return ticketRepository.findByUserEmail(email);
  }

  private Long createATicketForNewPurchase(TicketDto ticketDto, String email) {
    Optional<User> user = userRepository.findByUserEmail(email);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("No user found for user " + email);
    }
    TicketStatus newStatus;
    if (ticketDto.sealedFlag().equalsIgnoreCase("Y")) {
      newStatus = TicketStatus.LISTED;
    } else {
      newStatus = TicketStatus.QC1;
    }
    UUID invoiceUUID = UUID.randomUUID();
    Ticket newTicket = Ticket.builder()
        .userEmail(email)
        .clientId(user.get().getClientId())
        .ticketStatus(newStatus)
        .invoiceNumber(invoiceUUID.toString())
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
        .itemSerialNo(ticketDto.itemSerialNo())
        .imeiNo(ticketDto.imeiNo())
        .batteryHealth(ticketDto.batteryHealth())
        .warranty(ticketDto.warranty())
        .boxFlag(ticketDto.boxFlag())
        .chargerFlag(ticketDto.chargerFlag())
        .sealedFlag(ticketDto.sealedFlag())
        .invoiceFlag(ticketDto.invoiceFlag())
        .acquisitionCost(ticketDto.acquisitionCost())
        .refurbishedCost(ticketDto.refurbishedCost())
        .ramRomSpecs(ticketDto.ramRomSpecs())
        .colorSpecs(ticketDto.ColorSpecs())
        .comment(ticketDto.comments())
        .productName(ticketDto.productName())
        .build();
    ticketRepository.save(newTicket);

    TicketLifecycle ticketLifecycle = TicketLifecycle.builder()
        .ticketId(newTicket.getTicketId())
        .userEmail(email)
        .comment(ticketDto.comments())
        .statusChangeTime(Instant.now())
        .prevTicketStatus(null)
        .newTicketStatus(newStatus)
        .build();
    ticketLifeCycleRepository.save(ticketLifecycle);
    return newTicket.getTicketId();
  }

  @Override
  public List<Ticket> findTicketBySpecification(
      Map<String, String> allParams,
      String username
  ) {
    Set<String> CONTAINS_FIELDS = Set.of(
        "ramRomSpecs",
        "productName"
    );

    Specification<Ticket> spec = (root, cq, cb) -> {
      List<Predicate> preds = new ArrayList<>();

      preds.add(cb.equal(root.get("userEmail"), username));

      EntityType<Ticket> meta = root.getModel();
      for (var entry : allParams.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        if (value == null || value.isBlank()) {
          continue;
        }

        Class<?> javaType = meta.getAttribute(key).getJavaType();

        if (String.class.equals(javaType)) {
          if (CONTAINS_FIELDS.contains(key)) {
            preds.add(cb.like(
                cb.lower(root.get(key)),
                "%" + value.toLowerCase() + "%"
            ));
          } else {
            preds.add(cb.equal(root.get(key), value));
          }
        } else if (Number.class.isAssignableFrom(javaType)) {
          Long longVal = Long.valueOf(value);
          preds.add(cb.equal(root.get(key), longVal));
        } else if (Boolean.class.equals(javaType) || boolean.class.equals(javaType)) {
          Boolean boolVal = Boolean.valueOf(value);
          preds.add(cb.equal(root.get(key), boolVal));
        } else if (LocalDate.class.equals(javaType)) {
          LocalDate dateVal = LocalDate.parse(value);
          preds.add(cb.equal(root.get(key), dateVal));
        } else {
          preds.add(cb.like(
              root.get(key).as(String.class),
              "%" + value + "%"
          ));
        }
      }

      return cb.and(preds.toArray(new Predicate[0]));
    };

    return ticketRepository.findAll(spec);
  }


  @Override
  public BillDto checkBill(Long ticketId) {
    Optional<SoldStatus> bill = soldStatusRepository.findByTicketId(ticketId);
    if (bill.isEmpty()) {
      throw new IllegalArgumentException("No Bill found.");
    }
    return BillDto.builder()
        .clientId(bill.get().getClientId())
        .customerName(bill.get().getCustomerName())
        .phoneNumber(bill.get().getPhoneNumber())
        .modeOfPayment(bill.get().getModeOfPayment())
        .gstId(bill.get().getGstId())
        .modeOfPayment(bill.get().getModeOfPayment())
        .onlineTrxId(bill.get().getOnlineTrxId())
        .placeOfSale(bill.get().getPlaceOfSale())
        .billNumber(bill.get().getBillNumber())
        .billDate(bill.get().getBillDate())
        .profit(bill.get().getProfit())
        .build();
  }

  @Override
  public Ticket checkTicket(Long ticketId) {
    Optional<Ticket> ticket = ticketRepository.findByTicketId(ticketId);
    if (ticket.isEmpty()) {
      throw new IllegalArgumentException("No ticket found.");
    }
    return ticket.get();
  }


}
