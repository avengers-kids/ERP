package com.erp.erp.domain.model.client;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@SequenceGenerator(
    name = "whitelabel_global_seq",
    sequenceName = "WHITELABEL_GLOBAL_SEQ",
    allocationSize = 1
)
@Table(name = "WHITELABEL_CLIENT")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client {
    
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "whitelabel_global_seq"
    )
    @Column(name = "CLIENT_ID", nullable = false)
    private long clientId;
    
    @Column(name = "CLIENT_NAME", length = 100)
    private String clientName;
    
    @Column(name = "EMAIL", length = 255)
    private String email;
    
    @Column(name = "PHONE_NUMBER", length = 20)
    private String phoneNumber;

    @Column(name = "CREATED_BY", nullable = false, length = 240)
    @CreatedBy
    @NotNull(message = "createdBy cannot be empty")
    private String createdBy;

    @Column(name = "LAST_UPDATED_BY", nullable = false, length = 240)
    @LastModifiedBy
    @NotNull(message = "lastUpdatedBy cannot be empty")
    private String lastUpdatedBy;

    @Column(name = "CREATED_DATE", nullable = false)
    @JsonSerialize(using = InstantSerializer.class)
    @CreatedDate
    private Instant creationDate;


    @Column(name = "LAST_UPDATE_DATE", nullable = false)
    @JsonSerialize(using = InstantSerializer.class)
    @LastModifiedDate
    private Instant lastUpdateDate;

    @Column(name = "VERSION")
    @Version
    private long version;
}
