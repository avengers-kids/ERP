package com.erp.erp.application.dto;

import com.erp.erp.domain.model.ticket.Ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Data Transfer Object for Ticket entity.
 */
public record TicketDto(
    @NotNull(message = "clientId cannot be unspecified")
    Long clientId,
    @NotNull(message = "itemId cannot be unspecified")
    Long itemId,
    @NotNull(message = "invoiceNumber cannot be unspecified")
    Long invoiceNumber,
    @NotNull(message = "invoiceDate cannot be unspecified")
    LocalDate invoiceDate,
    String phoneNumber,
    @NotBlank(message = "customerName cannot be unspecified")
    String customerName,
    String gstNumber,
    String gstId,
    @NotBlank(message = "purchaseType cannot be unspecified")
    String productPurchaseType,
    String modeOfPayment,
    Long customerAadharId
) {

  /**
   * Converts a Ticket entity to its DTO representation.
   */
  public static TicketDto fromEntity(Ticket ticket) {
    return new TicketDto(
        ticket.getClientId(),
        ticket.getItemId(),
        ticket.getInvoiceNumber(),
        ticket.getInvoiceDate(),
        ticket.getPhoneNumber(),
        ticket.getCustomerName(),
        ticket.getGstNumber(),
        ticket.getGstId(),
        ticket.getProductPurchaseType(),
        ticket.getModeOfPayment(),
        ticket.getCustomerAadharId()
    );
  }

  @Override
  public String toString() {
    return "TicketDto{" +
        ", clientId=" + clientId +
        ", invoiceNumber=" + invoiceNumber +
        ", invoiceDate=" + invoiceDate +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", customerName='" + customerName + '\'' +
        ", gstNumber='" + gstNumber + '\'' +
        ", gstId='" + gstId + '\'' +
        ", productPurchaseType='" + productPurchaseType + '\'' +
        ", modeOfPayment='" + modeOfPayment + '\'' +
        ", customerAadharId=" + customerAadharId +
        '}';
  }
}
