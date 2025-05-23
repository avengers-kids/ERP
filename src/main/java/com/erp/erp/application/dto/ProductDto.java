package com.erp.erp.application.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for Product entity.
 */
public record ProductDto(
    @NotNull(message = "product name cannot be unspecified")
    String productName,
//    @NotNull(message = "product description cannot be unspecified")
    String productDescription

) {

  @Override
  public String toString() {
    return "ProductDto{" +
        " productName=" + productName +
        ", productDescription=" + productDescription +
        '}';
  }
}
