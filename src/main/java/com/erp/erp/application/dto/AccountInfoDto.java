package com.erp.erp.application.dto;

import java.time.LocalDate;
import java.util.Map;
import lombok.Builder;

/**
 * Data Transfer Object for User Account Info
 */
@Builder
public record AccountInfoDto(
    String legalName,
    String userName,
    String phoneNumber,
    LocalDate dateOfBirth,
    String userEmail,
    String userRoles,
    Map<Long, String> stores
) {

  @Override
  public String toString() {
    return "AccountInfoDto";
  }
}
