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
@Table(name = "WHITELABEL_TICKET_REMARK")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TicketRemark extends AbstractEntity {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "whitelabel_global_seq"
    )
    @Column(name = "TICKET_REMARK_ID", nullable = false)
    private Long ticketRemarkId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "TICKET_ID", nullable = false)
    private Long ticketId;

    @Column(name = "TICKET_STATUS", nullable = false, length = 20)
    @NotBlank
    private String ticketStatus;

    @Column(name = "TICKET_REMARK", length = 500)
    private String ticketRemark;

    @Column(name = "REMARK_TIME", nullable = false)
    private Instant remarkTime;

    @Column(name = "IS_DELETED", length = 1)
    private String isDeleted;

}
