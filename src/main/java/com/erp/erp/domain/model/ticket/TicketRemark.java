package com.erp.erp.domain.model.ticket;

import com.erp.erp.domain.model.shared.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WHITELABEL_TICKET_REMARK")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TicketRemark extends AbstractEntity {

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
    @Column(name = "TICKET_REMARK_ID", nullable = false)
    private Long ticketRemarkId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "TICKET_ID", nullable = false)
    private Long ticketId;

    @Column(name = "TICKET_STATUS", nullable = false, length = 20)
    @NotNull
    private String ticketStatus;

    @Column(name = "TICKET_REMARK", length = 500)
    private String ticketRemark;

    @Column(name = "REMARK_TIME", nullable = false)
    private Instant remarkTime;

    @Column(name = "IS_DELETED", length = 1)
    private String isDeleted;

}
