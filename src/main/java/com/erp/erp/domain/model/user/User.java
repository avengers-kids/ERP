package com.erp.erp.domain.model.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Builder
@Table(name = "WHITELABEL_USER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "whitelabel_sequence")
//    @SequenceGenerator(name = "whitelabel_sequence", sequenceName = "whitelabel.admin.whitelabel_sequence", schema = "admin", allocationSize = 20)
    @Column(name = "USER_ID", nullable = false)
    private long userId;

    @Column(name = "USER_NAME", nullable = false, length = 100)
    @NotBlank(message = "User name cannot be empty")
    private String userName;

    @Column(name = "PASSWORD", nullable = false, length = 1000)
    @NotBlank(message = "password cannot be empty")
    private String password;

    @Column(name = "CLIENT_ID", nullable = false)
    @NotNull(message = "clientId cannot be empty")
    private Long clientId;

    @Column(name = "USER_EMAIL", nullable = false, length = 255)
    @NotBlank(message = "userEmail cannot be empty")
    private String userEmail;

    @Column(name = "USER_PHONE_NUMBER", length = 20)
    private String userPhoneNumber;

    @Column(name = "USER_ROLES", length = 20)
    private String userRoles;

    @Column(name = "IS_DELETED", length = 1)
    private String isDeleted;

    @Column(name = "CREATED_BY", nullable = false, length = 240)
    @CreatedBy
    @NotBlank(message = "createdBy cannot be empty")
    private String createdBy;

    @Column(name = "LAST_UPDATED_BY", nullable = false, length = 240)
    @LastModifiedBy
    @NotBlank(message = "lastUpdatedBy cannot be empty")
    private String lastUpdatedBy;

    @Column(name = "CREATED_DATE", nullable = false)
    @JsonSerialize(using = InstantSerializer.class)
    @CreatedDate
    @NotBlank(message = "lastUpdatedBy cannot be empty")
    private Instant creationDate;


    @Column(name = "LAST_UPDATE_DATE", nullable = false)
    @JsonSerialize(using = InstantSerializer.class)
    @LastModifiedDate
    @NotBlank(message = "lastUpdatedBy cannot be empty")
    private Instant lastUpdateDate;

    @Column(name = "VERSION", nullable = false)
    @Version
    @NotBlank(message = "lastUpdatedBy cannot be empty")
    private long version;

}
