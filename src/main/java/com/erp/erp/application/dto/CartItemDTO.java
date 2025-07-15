package com.erp.erp.application.dto;

import java.util.List;

public record CartItemDTO(
    Long id,
    Long itemId,
    Integer quantity,
    List<CartItemDetailDTO> details
) {}