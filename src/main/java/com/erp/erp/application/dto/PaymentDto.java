package com.erp.erp.application.dto;

import com.erp.erp.domain.enums.PaymentMode;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDto {
  private PaymentMode modeOfPayment;
  private String transactionId;
  private BigDecimal amount;
  private LocalDate paidAt = LocalDate.now();
}