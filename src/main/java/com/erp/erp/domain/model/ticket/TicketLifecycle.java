package com.erp.erp.domain.model.ticket;

import com.erp.erp.domain.model.shared.AbstractEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

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

    @Column(name = "PREV_TICKET_STATUS", length = 20)
    private String prevTicketStatus;

    @Column(name = "NEW_TICKET_STATUS", nullable = false, length = 20)
    @NotBlank
    private String newTicketStatus;

    @Column(name = "TICKET_ID", nullable = false)
    private Long ticketId;

    @Column(name = "STATUS_CHANGE_TIME", nullable = false)
    private Instant statusChangeTime;

    @Column(name = "IS_DELETED", length = 1)
    private String isDeleted;

}
