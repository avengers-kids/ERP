package com.erp.erp.domain.model.client;

import com.erp.erp.domain.model.shared.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "WHITELABEL_CLIENT")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client extends AbstractEntity {
    
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
    @Column(name = "CLIENT_ID", nullable = false)
    private long clientId;
    
    @Column(name = "CLIENT_NAME", length = 100)
    private String clientName;
    
    @Column(name = "EMAIL", length = 255)
    private String email;
    
    @Column(name = "PHONE_NUMBER", length = 20)
    private String phoneNumber;

}
