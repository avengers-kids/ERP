package com.erp.erp.application.ticket;

import org.springframework.security.core.AuthenticationException;

public class ClientNotFoundException extends AuthenticationException {

  public ClientNotFoundException(String msg) {
    super(msg);
  }

  public ClientNotFoundException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
