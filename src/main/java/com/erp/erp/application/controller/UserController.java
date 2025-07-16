package com.erp.erp.application.controller;

import com.erp.erp.application.dto.AccountInfoDto;
import com.erp.erp.application.login.AuthService;
import com.erp.erp.application.product.ProductService;
import com.erp.erp.domain.model.item.ProductMaster;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

  private final AuthService authService;

  public UserController(AuthService authService) {
    this.authService = authService;
  }

  @GetMapping("/my-account")
  @PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
  public ResponseEntity<AccountInfoDto> fetchAccountInfo(
      @AuthenticationPrincipal(expression = "username") String username) {
    return ResponseEntity.ok(authService.fetchUserInfo(username));
  }

}
