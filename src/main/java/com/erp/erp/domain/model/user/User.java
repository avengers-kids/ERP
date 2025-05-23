package com.erp.erp.domain.model.user;

import com.erp.erp.domain.model.shared.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "WHITELABEL_USER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends AbstractEntity {

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
    @Column(name = "USER_ID", nullable = false)
    private long userId;

    @Column(name = "USER_NAME", nullable = false, length = 100)
    @NotNull(message = "User name cannot be empty")
    private String userName;

    @Column(name = "PASSWORD", nullable = false, length = 1000)
    @NotNull(message = "password cannot be empty")
    private String password;

    @Column(name = "CLIENT_ID", nullable = false)
    @NotNull(message = "clientId cannot be empty")
    private Long clientId;

    @Column(name = "USER_EMAIL", nullable = false, length = 255)
    @NotNull(message = "userEmail cannot be empty")
    private String userEmail;

    @Column(name = "USER_PHONE_NUMBER", length = 20)
    private String userPhoneNumber;

    @Column(name = "USER_ROLES", length = 20)
    private String userRoles;

    @Column(name = "IS_DELETED", length = 1)
    private String isDeleted;
    public void changePassword(String userEmail, String newPwd) {
        if (this.userEmail.equals(userEmail)) {
            this.password = newPwd;
        }
    }

}
