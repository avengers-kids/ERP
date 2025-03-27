package com.erp.erp.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ClientSignupRequest {
  private Long clientId;
  private String clientName;
  private String clientEmail;
  private String clientPhoneNumber;
}
