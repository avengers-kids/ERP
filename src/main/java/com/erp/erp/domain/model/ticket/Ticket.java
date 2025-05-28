package com.erp.erp.domain.model.ticket;

import com.erp.erp.domain.enums.TicketStatus;
import com.erp.erp.domain.model.shared.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WHITELABEL_TICKET")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Ticket extends AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ticketSeqGen")
  @TableGenerator(
      name           = "ticketSeqGen",
      table          = "global_sequence",
      pkColumnName   = "seq_name",
      valueColumnName= "next_val",
      pkColumnValue  = "ticket_seq",
      initialValue   = 100000,
      allocationSize = 1
  )
  @Column(name = "TICKET_ID", nullable = false)
  private Long ticketId;

  @Column(name = "CLIENT_ID", nullable = false)
  @NotNull
  private Long clientId;

  @Enumerated(EnumType.STRING)
  @Column(name = "TICKET_STATUS", nullable = false, length = 20)
  @NotNull
  private TicketStatus ticketStatus;

  @Column(name = "INVOICE_NUMBER", length = 300)
  private String invoiceNumber;

  @Column(name = "INVOICE_DATE")
  private LocalDate invoiceDate;

  @Column(name = "PHONE_NUMBER", length = 20)
  private String phoneNumber;

  @Column(name = "CUSTOMER_NAME", length = 100)
  private String customerName;

  @Column(name = "GST_NUMBER", length = 100)
  private String gstNumber;

  @Column(name = "GST_ID", length = 100)
  private String gstId;

  @Column(name = "PRODUCT_PURCHASE_TYPE", length = 50)
  private String productPurchaseType;

  @Column(name = "MODE_OF_PAYMENT", length = 20)
  private String modeOfPayment;

  @Column(name = "ACQUISITION_COST")
  private BigDecimal acquisitionCost;

  @Column(name = "REFURBISHED_COST")
  private BigDecimal refurbishedCost;

  @Column(name = "CUSTOMER_AADHAR_ID")
  private Long customerAadharId;

  @Column(name = "ITEM_ID", nullable = false)
  private Long itemId;

  @Column(name = "USER_EMAIL", nullable = false)
  private String userEmail;

  @Column(name = "IS_DELETED", length = 1)
  private String isDeleted;

}
