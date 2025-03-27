package com.erp.erp.domain.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
public class LogInResponse {
  private HttpStatus status;
  private String userEmail;
  private String message;
}
