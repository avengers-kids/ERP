package com.erp.erp.domain.model.user;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WHITELABEL_USER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "whitelabel_seq")
    @SequenceGenerator(name = "whitelabel_seq", sequenceName = "admin.whitelabel_sequence", allocationSize = 20)
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "USER_NAME", nullable = false, length = 100)
    private String userName;

    @Column(name = "PASSWORD", length = 500)
    private String password;

    @Column(name = "CLIENT_ID", nullable = false)
    private Long clientId;

    @Column(name = "USER_EMAIL", length = 255)
    private String userEmail;

    @Column(name = "USER_PHONE_NUMBER", length = 20)
    private String userPhoneNumber;

    @Column(name = "USER_ROLES", length = 20)
    private String userRoles;

    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "IS_DELETED", length = 1)
    private String isDeleted;

    @Column(name = "CREATED_DATE", columnDefinition = "TIMESTAMP(6) DEFAULT systimestamp")
    private Instant createdDate;

    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Column(name = "LAST_UPDATE_DATE", columnDefinition = "TIMESTAMP(6) DEFAULT systimestamp")
    private Instant lastUpdateDate;

    @Column(name = "LAST_UPDATED_BY", length = 50)
    private String lastUpdatedBy;

}
