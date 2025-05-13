package com.erp.erp.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserSignupRequest {
  private Long userId;
  private String userEmail;
  private Long clientId;
  private String userPhoneNumber;
  private String userRoles;
  private String password;
}
