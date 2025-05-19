package com.erp.erp.application.dto;

import com.erp.erp.domain.model.ticket.Ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
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
    Long customerAadharId,
    String phoneName,
    BigDecimal phonePrice,
    String variant,
    String color,
    String itemSerialNo,
    String imeiNo,
    String batteryHealth,
    String warranty,
    String boxFlag,
    String chargerFlag,
    String sealedFlag,
    String invoiceFlag

) {

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
