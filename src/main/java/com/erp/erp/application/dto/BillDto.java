package com.erp.erp.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

/**
 * Data Transfer Object for Bill entity.
 */
@Builder
public record BillDto(
    Long clientId,
    String phoneNumber,
    String customerName,
    String gstId,
    String modeOfPayment,
    String onlineTrxId,
    String placeOfSale,
    BigDecimal profit,
    String billNumber,
    LocalDate billDate

) {

  @Override
  public String toString() {
    return "BillDto";
  }
}
