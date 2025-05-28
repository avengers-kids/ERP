package com.erp.erp.domain.model.shared;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.Setter;

/**
 * Base abstract entity providing auditing fields and optimistic locking.
 * Enable JPA auditing with @EnableJpaAuditing in a configuration class or main application.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AbstractEntity {

  @Version
  @Column(name = "VERSION", nullable = false)
  private Long version;

  @CreatedDate
  @Column(name = "CREATED_DATE", nullable = false, updatable = false)
  private Instant createdDate;

  @LastModifiedDate
  //change instant to long
  @Column(name = "LAST_UPDATE_DATE", nullable = false)
  private Instant lastUpdateDate;

  @CreatedBy
  @Column(name = "CREATED_BY", nullable = false, updatable = false, length = 50)
  private String createdBy;

  @LastModifiedBy
  @Column(name = "LAST_UPDATED_BY", nullable = false, length = 50)
  private String lastUpdatedBy;
}