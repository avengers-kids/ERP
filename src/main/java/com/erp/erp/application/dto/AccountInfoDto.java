package com.erp.erp.application.dto;

import java.time.LocalDate;
import lombok.Builder;

/**
 * Data Transfer Object for User Account Info
 */
@Builder
public record AccountInfoDto(
    String legalName,
    String userName,
    String phoneNumber,
    String storeName,
    LocalDate dateOfBirth,
    String userEmail,
    String userRoles
) {

  @Override
  public String toString() {
    return "AccountInfoDto";
  }
}
