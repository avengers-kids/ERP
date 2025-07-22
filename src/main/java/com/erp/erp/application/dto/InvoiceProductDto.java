package com.erp.erp.application.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceProductDto {
  private Long ticketId;
  private Long itemId;
  private String productName;
  private String brand;
  private String ramRomSpecs;
  private String colorSpecs;
  private BigDecimal acquisitionCost;
  private BigDecimal refurbishedCost;
  private String imeiNo;
  private String serialNo;
  private String boxFlag;
  private String chargerFlag;
  private String sealedFlag;
  private String warranty;
}
