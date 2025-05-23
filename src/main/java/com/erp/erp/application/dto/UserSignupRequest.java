package com.erp.erp.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserSignupRequest {
  @NotNull
  private String userEmail;
  @NotNull
  private Long clientId;
  private String userPhoneNumber;
  @NotNull
  private String userRoles;
  @NotNull
  private String password;
}
