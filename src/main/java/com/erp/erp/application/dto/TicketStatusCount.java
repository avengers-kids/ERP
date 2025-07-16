package com.erp.erp.application.dto;

import com.erp.erp.domain.enums.TicketStatus;

public interface TicketStatusCount {
    TicketStatus getStatus();
    Long getCount();
}
