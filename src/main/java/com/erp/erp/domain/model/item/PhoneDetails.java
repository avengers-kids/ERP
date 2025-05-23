package com.erp.erp.domain.model.item;

import com.erp.erp.domain.model.shared.AbstractEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "PHONE_DETAILS")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PhoneDetails extends AbstractEntity {

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
    @Column(name = "PHONE_DETAILS_ID", nullable = false)
    private Long phoneDetailsId;

    @Column(name = "ITEM_ID", nullable = false)
    private Long itemId;

    @Column(name = "TICKET_ID")
    private Long ticketId;

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "VARIANT", length = 50)
    private String variant;

    @Column(name = "COLOR", length = 20)
    private String color;

    @Column(name = "ITEM_SERIAL_NO", length = 50)
    private String itemSerialNo;

    @Column(name = "IMEI_NO", length = 20)
    private String imeiNo;

    @Column(name = "BATTERY_HEALTH", length = 20)
    private String batteryHealth;

    @Column(name = "WARRANTY", length = 50)
    private String warranty;

    @Column(name = "BOX_FLAG", length = 1)
    @Size(max = 1)
    private String boxFlag;

    @Column(name = "CHARGER_FLAG", length = 1)
    @Size(max = 1)
    private String chargerFlag;

    @Column(name = "SEALED_PACK_FLAG", length = 1)
    @Size(max = 1)
    private String sealedPackFlag;

    @Column(name = "INVOICE_FLAG", length = 1)
    @Size(max = 1)
    private String invoiceFlag;

    @Column(name = "IS_DELETED", length = 1)
    @Size(max = 1)
    private String isDeleted;

}
