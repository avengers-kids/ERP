package com.erp.erp.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ClientSignupRequest {
  private String clientName;
  private String clientEmail;
  private String clientPhoneNumber;
}
