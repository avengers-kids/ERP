package com.erp.erp.domain.model.ticket;

import com.erp.erp.domain.model.shared.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "CUSTOMER_NAME", nullable = false)
    private String customerName;

    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;

    @Column(name = "GST_ID")
    private String gstId;

    @Column(name = "MODE_OF_PAYMENT", nullable = false)
    private String modeOfPayment;

    @Column(name = "ONLINE_TRX_ID")
    private String onlineTrxId;

    @Column(name = "PLACE_OF_SALE")
    private String placeOfSale;

    @Column(name = "PROFIT")
    private BigDecimal profit;

    @Column(name = "BILL_NUMBER", nullable = false)
    private String billNumber;

    @Column(name = "BILL_DATE", nullable = false)
    private LocalDate billDate;

    @Column(name = "IS_DELTED", nullable = false, length = 1)
    private String isDeleted;
}
