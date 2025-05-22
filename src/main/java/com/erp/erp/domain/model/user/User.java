package com.erp.erp.domain.model.user;

import com.erp.erp.domain.model.shared.AbstractEntity;
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
@SequenceGenerator(
    name = "whitelabel_global_seq",
    sequenceName = "WHITELABEL_GLOBAL_SEQ",
    allocationSize = 1
)
@Builder
@Table(name = "WHITELABEL_USER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends AbstractEntity {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "whitelabel_global_seq"
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
