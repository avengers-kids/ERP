package com.erp.erp.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserSignupRequest {
  private String userEmail;
  private Long clientId;
  private String userPhoneNumber;
  private String userRoles;
  private String password;
}
