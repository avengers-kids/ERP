package com.erp.erp.application.dto;

import com.erp.erp.domain.enums.PaymentMode;
import java.util.List;
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
    Long customerAadharId,
    Long storeId,
    List<PaymentDto> payments

) {

  @Override
  public String toString() {
    return "TicketDto{" +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", customerName='" + customerName + '\'' +
        ", gstNumber='" + gstNumber + '\'' +
        ", gstId='" + gstId + '\'' +
        ", productPurchaseType='" + productPurchaseType + '\'' +
        ", customerAadharId=" + customerAadharId +
        '}';
  }
}
