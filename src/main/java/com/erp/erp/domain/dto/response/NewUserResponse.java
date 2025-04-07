package com.erp.erp.domain.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
public class NewUserResponse {
  private HttpStatus status;
  private String userEmail;
  private String password;
  private String message;
}
