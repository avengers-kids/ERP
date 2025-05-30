package com.erp.erp.domain.model.ticket;

import com.erp.erp.domain.model.shared.AbstractEntity;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import lombok.*;

/**
 * JPA entity mapping for the WHITELABEL_SOLD_STATUS_TABLE.
 */
@Entity
@Table(name = "WHITELABEL_SOLD_STATUS_TABLE")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SoldStatus extends AbstractEntity {

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
    @Column(name = "SOLD_TABLE_ID", nullable = false)
    private Long soldTableId;

    @Column(name = "TICKET_ID", nullable = false)
    private Long ticketId;

    @Column(name = "CLIENT_ID", nullable = false)
    private Long clientId;

    @Column(name = "INVOICE_NUMBER", length = 300, nullable = false)
    private String invoiceNumber;

    @Column(name = "INVOICE_DATE", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "PHONE_NUMBER", length = 20)
    private String phoneNumber;

    @Column(name = "CUSTOMER_NAME", length = 100)
    private String customerName;

    @Column(name = "GST_NUMBER", length = 100)
    private String gstNumber;

    @Column(name = "GST_ID", length = 100)
    private String gstId;

    @Column(name = "PURCHASE_TYPE", length = 50)
    private String purchaseType;

    @Column(name = "MODE_OF_PAYMENT", length = 20)
    private String modeOfPayment;

    @Column(name = "CUSTOMER_AADHAR_ID")
    private Long customerAadharId;

    @Column(name = "ITEM_ID")
    private Long itemId;

    @Column(name = "IS_DELTED", nullable = false, length = 1)
    private String isDeleted;
}
