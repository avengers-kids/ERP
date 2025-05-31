package com.erp.erp.domain.model.ticket;

import com.erp.erp.domain.enums.TicketStatus;
import com.erp.erp.domain.model.shared.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WHITELABEL_TICKET_LIFECYCLE")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TicketLifecycle extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ticketSeqGen")
    @TableGenerator(
        name           = "ticketSeqGen",
        table          = "global_sequence",
        pkColumnName   = "seq_name",
        valueColumnName= "next_val",
        pkColumnValue  = "ticket_seq",
        initialValue   = 100000,
        allocationSize = 1
    )
    @Column(name = "TICKET_LC_ID", nullable = false)
    private Long ticketLcId;

    @Enumerated(EnumType.STRING)
    @Column(name = "PREV_TICKET_STATUS", length = 20)
    private TicketStatus prevTicketStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "NEW_TICKET_STATUS", nullable = false, length = 20)
    @NotNull
    private TicketStatus newTicketStatus;

    @Column(name = "TICKET_ID", nullable = false)
    private Long ticketId;

    @Column(name = "STATUS_CHANGE_TIME", nullable = false)
    private Instant statusChangeTime;

    @Column(name = "USER_EMAIL", nullable = false)
    private String userEmail;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "IS_DELETED", length = 1)
    private String isDeleted;

    @Column(name = "COST_AGGREGATION")
    private BigDecimal costAggregation;

}
