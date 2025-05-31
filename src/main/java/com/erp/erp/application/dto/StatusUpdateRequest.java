package com.erp.erp.application.dto;

import com.erp.erp.domain.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Payload for updating a ticket's status.
 */
public record StatusUpdateRequest(
    @NotNull(message = "newStatus cannot be null")
    TicketStatus newStatus,

    String comment,
    BigDecimal cost
) {

    // Compact canonical constructor:
    public StatusUpdateRequest {
        if (cost == null) {
            cost = BigDecimal.ZERO;
        }
    }
}
