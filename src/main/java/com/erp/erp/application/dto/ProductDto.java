package com.erp.erp.application.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for Product entity.
 */
public record ProductDto(

    @NotNull(message = "product ID cannot be unspecified")
    Long productMasterId,
    @NotNull(message = "product name cannot be unspecified")
    String productName,
//    @NotNull(message = "product description cannot be unspecified")
    String productDescription

) {

  @Override
  public String toString() {
    return "ProductDto{" +
        "productId=" + productMasterId +
        " productName=" + productName +
        ", productDescription=" + productDescription +
        '}';
  }
}
