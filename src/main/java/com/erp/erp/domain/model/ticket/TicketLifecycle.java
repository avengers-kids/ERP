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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@SequenceGenerator(
    name = "whitelabel_global_seq",
    sequenceName = "WHITELABEL_GLOBAL_SEQ",
    allocationSize = 1
)
@Table(name = "WHITELABEL_TICKET_LIFECYCLE")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TicketLifecycle extends AbstractEntity {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "whitelabel_global_seq"
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

    @Column(name = "IS_DELETED", length = 1)
    private String isDeleted;

}
