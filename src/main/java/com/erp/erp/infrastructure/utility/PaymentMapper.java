package com.erp.erp.infrastructure.utility;

import com.erp.erp.application.dto.PaymentDto;
import com.erp.erp.domain.model.payment.Payment;

public class PaymentMapper {

  public static PaymentDto toDto(Payment payment) {
    if (payment == null) {
      return null;
    }

    return PaymentDto.builder()
        .modeOfPayment(payment.getModeOfPayment())
        .transactionId(payment.getTransactionId())
        .amount(payment.getAmount())
        .paidAt(payment.getPaidAt())
        .build();
  }
}
