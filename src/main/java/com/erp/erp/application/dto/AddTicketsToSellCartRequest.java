package com.erp.erp.application.dto;

import java.util.List;
import lombok.Data;

@Data
public class AddTicketsToSellCartRequest {
    private List<Long> ticketIds;
}
