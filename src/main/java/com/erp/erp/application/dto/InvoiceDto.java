package com.erp.erp.application.dto;

import com.erp.erp.domain.enums.PaymentMode;
import java.time.LocalDate;
import lombok.Builder;

/**
 * Data Transfer Object for Invoice Data
 */
@Builder
public record InvoiceDto(
    String phoneNumber,
    String customerName,
    String gstNumber,
    String gstId,
    String productPurchaseType,
    PaymentMode modeOfPayment,
    Long customerAadharId,
    Long storeId

) {

  @Override
  public String toString() {
    return "TicketDto{" +
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
