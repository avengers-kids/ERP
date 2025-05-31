package com.erp.erp.application.dto;

import com.erp.erp.domain.model.ticket.Ticket;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

/**
 * Data Transfer Object for Ticket entity.
 */
@Builder
public record TicketDto(
    Long itemId,
    String invoiceNumber,
    LocalDate invoiceDate,
    String phoneNumber,
    String customerName,
    String gstNumber,
    String gstId,
    String productPurchaseType,
    String modeOfPayment,
    Long customerAadharId,
    String itemSerialNo,
    String imeiNo,
    String batteryHealth,
    String warranty,
    String boxFlag,
    String chargerFlag,
    String sealedFlag,
    String invoiceFlag,
    BigDecimal acquisitionCost,
    BigDecimal refurbishedCost,
    String ramRomSpecs,
    String ColorSpecs,
    String comments,
    String productName

) {

  @Override
  public String toString() {
    return "TicketDto{" +
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

  /**
   * Static factory method to build a TicketDto from a Ticket entity.
   */
  public static TicketDto fromEntity(Ticket ticket) {
    return TicketDto.builder()
        .itemId(ticket.getItemId())
        .invoiceNumber(ticket.getInvoiceNumber())
        .invoiceDate(ticket.getInvoiceDate())
        .phoneNumber(ticket.getPhoneNumber())
        .customerName(ticket.getCustomerName())
        .gstNumber(ticket.getGstNumber())
        .gstId(ticket.getGstId())
        .productPurchaseType(ticket.getProductPurchaseType())
        .modeOfPayment(ticket.getModeOfPayment())
        .customerAadharId(ticket.getCustomerAadharId())
        .itemSerialNo(ticket.getItemSerialNo())
        .imeiNo(ticket.getImeiNo())
        .batteryHealth(ticket.getBatteryHealth())
        .warranty(ticket.getWarranty())
        .boxFlag(ticket.getBoxFlag())
        .chargerFlag(ticket.getChargerFlag())
        .sealedFlag(ticket.getSealedFlag())
        .invoiceFlag(ticket.getInvoiceFlag())
        .acquisitionCost(ticket.getAcquisitionCost())
        .refurbishedCost(ticket.getRefurbishedCost())
        .ramRomSpecs(ticket.getRamRomSpecs())
        .ColorSpecs(ticket.getColorSpecs())
        .comments(ticket.getComment())
        .productName(ticket.getProductName())
        .build();
  }
}
